package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestMessage;
import com.java.java_proj.dto.request.forupdate.URequestMessageProfile;
import com.java.java_proj.dto.request.forupdate.URequestMessageStatus;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.entities.*;
import com.java.java_proj.entities.enums.MessageStatusType;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.services.templates.MessageService;
import com.java.java_proj.services.templates.NotificationService;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "messages")
public class MessageServiceImpl implements MessageService {

    final private MessageRepository messageRepository;
    final private ChannelRepository channelRepository;
    final private InboxRepository inboxRepository;
    final private ResourceService resourceService;
    final private UserService userService;
    final private NotificationService notificationService;
    final private RedisService redisService;
    final private ModelMapper modelMapper;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, ChannelRepository channelRepository, InboxRepository inboxRepository, ResourceService resourceService, UserService userService, NotificationService notificationService, RedisService redisService, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.inboxRepository = inboxRepository;
        this.resourceService = resourceService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.redisService = redisService;
        this.modelMapper = modelMapper;
    }

    @Override
//    @Cacheable(key = "'group-' + #channelId + '-' + {#content, #pageNo, #pageSize}")
    @Transactional
    public Page<DResponseMessage> getMessagesFromChannel(Integer channelId, String content, Integer pageNo, Integer pageSize) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));

        // fetch entity page
        Sort sort = Sort.by("id").descending();
        // if content == "", search by normal format (nested replies)
        boolean fullFormat = content.isEmpty();
        List<Message> fullMessageList = messageRepository.findAllByChannel(channel, sort, fullFormat);
        System.out.println(fullMessageList.size());
        fullMessageList = fullMessageList.stream()
                .filter(m -> m.getContent().toLowerCase().contains(content.toLowerCase()))
                .peek(m -> {
                    if (!fullFormat)
                        m.setReplies(new ArrayList<>());
                })
                .toList();

        int fromIndex = pageNo * pageSize, toIndex = (pageNo + 1) * pageSize, size = fullMessageList.size();

        if (fromIndex < size)
            fullMessageList = fullMessageList.subList(fromIndex, Math.min(toIndex, size));
        else
            fullMessageList = new ArrayList<>();

        // create pageable
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        return mapToDTO(new PageImpl<>(fullMessageList, pageable, fullMessageList.size()));
    }

    @Override
    @Cacheable(key = "'group-' + #inboxId + '-' + {#content, #pageNo, #pageSize}")
    @Transactional
    public Page<DResponseMessage> getMessagesFromInbox(Integer inboxId, String content, Integer pageNo, Integer pageSize) {

        Inbox inbox = inboxRepository.findById(inboxId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Inbox not found."));


        // fetch entity page
        Sort sort = Sort.by("id").descending();
        List<Message> fullMessageList = messageRepository.findAllByInbox(inbox, sort);
        fullMessageList = fullMessageList.stream()
                .filter(m -> m.getContent().contains(content))
                .toList();

        int fromIndex = pageNo * pageSize, toIndex = (pageNo + 1) * pageSize, size = fullMessageList.size();
        if (fromIndex < size)
            fullMessageList = fullMessageList.subList(fromIndex, Math.min(toIndex, size));
        else
            fullMessageList = new ArrayList<>();

        // create pageable
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        return mapToDTO(new PageImpl<>(fullMessageList, pageable, fullMessageList.size()));
    }

    @Override
    @Transactional
    public void sendMessageToChannel(Integer locationId, CRequestMessage requestMessage) {

        // get channel
        Channel channel = channelRepository.findById(locationId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));

        // build message
        User owner = userService.getOwner();
        Message message = Message.builder()
                .content(requestMessage.getContent())
                .resources(requestMessage.getResourceFiles().stream()
                        .map(resourceService::addFile)
                        .collect(Collectors.toList()))
                .createdBy(owner)
                .modifiedDate(LocalDateTime.now())
                .status(MessageStatusType.ACTIVE)
                .build();
        channel.getMessages().add(message);
        channel.setModifiedBy(owner);
        channel.setModifiedDate(LocalDateTime.now());

        // process replied message
        if (requestMessage.getReplyTo() != null) {
            Message repliedMessage = messageRepository.findByIdAndChannel(requestMessage.getReplyTo(), channel).orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Replied message not found"));

            message.setReplyTo(repliedMessage);
        }

        channelRepository.save(channel);

        // create notification and send socket message
        notificationService.messageToChannel(locationId);

        // evict message group
        redisService.evictKeysByPrefix("messages", "group-" + locationId);
    }

    @Override
    @Transactional
    public void sendMessageToInbox(Integer locationId, CRequestMessage requestMessage) {

        // get inbox
        Inbox inbox = inboxRepository.findById(locationId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Inbox not found."));

        // build message
        User owner = userService.getOwner();
        Message message = Message.builder()
                .content(requestMessage.getContent())
                .resources(requestMessage.getResourceFiles().stream()
                        .map(resourceService::addFile)
                        .collect(Collectors.toList()))
                .createdBy(owner)
                .modifiedDate(LocalDateTime.now())
                .status(MessageStatusType.ACTIVE)
                .build();
        inbox.getMessages().add(message);
        inbox.setModifiedBy(owner);
        inbox.setModifiedDate(LocalDateTime.now());

        // process replied message
        if (requestMessage.getReplyTo() != null) {
            Message repliedMessage = messageRepository.findByIdAndInbox(requestMessage.getReplyTo(), inbox).orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Replied message not found"));

            message.setReplyTo(repliedMessage);
        }

        inboxRepository.save(inbox);

        // create notification and send socket message
        notificationService.messageToInbox(locationId);

        // evict message group
        redisService.evictKeysByPrefix("messages", "group-" + locationId);
    }

    @Override
    public void updateMessageContent(Integer locationId, URequestMessageProfile requestMessage) {

        Message message = messageRepository.findById(requestMessage.getId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Message not found."));

        // check if message belong to that location
        if (messageRepository.countByLocation(message, locationId) == 0)
            throw new HttpException(HttpStatus.FORBIDDEN, "Message don't belong to that location.");

        // check if message owner
        if (!Objects.equals(message.getCreatedBy().getId(), userService.getOwner().getId()))
            throw new HttpException(HttpStatus.FORBIDDEN, "Can't change others message.");

        // check if message is active
        if (message.getStatus() == MessageStatusType.INACTIVE)
            throw new HttpException(HttpStatus.FORBIDDEN, "Message is deleted.");

        message.setContent(requestMessage.getContent());
        message.setModifiedDate(LocalDateTime.now());

        messageRepository.save(message);

        // evict message group
        redisService.evictKeysByPrefix("messages", "group-" + locationId);
    }

    @Override
    public void updateMessageStatus(Integer locationId, URequestMessageStatus requestMessage) {

        // check and set
        Message message = messageRepository.findById(requestMessage.getId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Message not found."));

        // check if message belong to that location
        if (messageRepository.countByLocation(message, locationId) == 0)
            throw new HttpException(HttpStatus.FORBIDDEN, "Message don't belong to that location.");

        message.setStatus(requestMessage.getStatus());
        message.setModifiedDate(LocalDateTime.now());

        messageRepository.save(message);

        // evict message group
        redisService.evictKeysByPrefix("messages", "group-" + locationId);
    }

    @Override
    public void deleteMessage(Integer locationId, Integer messageId) {

        // check exist and owner
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Message not found."));
        if (!Objects.equals(message.getCreatedBy().getId(), userService.getOwner().getId()))
            throw new HttpException(HttpStatus.FORBIDDEN, "Can't delete others message");

        // set status
        message.setStatus(MessageStatusType.INACTIVE);
        message.setModifiedDate(LocalDateTime.now());

        messageRepository.save(message);

        // evict message group
        redisService.evictKeysByPrefix("messages", "group-" + locationId);
    }

    @Override
    @Transactional
    public void seeInboxMessage(Integer inboxId, Integer messageId, Integer ownerId) {

        // check message
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Message not found."));

        // check if message belong to that location
        if (messageRepository.countByLocation(message, inboxId) == 0)
            throw new HttpException(HttpStatus.FORBIDDEN, "Message don't belong to that location.");

        Inbox inbox = inboxRepository.findById(inboxId)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "Inbox not found."));
        if (!inbox.getIsActive()) throw new HttpException(HttpStatus.BAD_REQUEST, "Invalid inbox");

        UserFriendship friendship = inbox.getFriendship();
        if (Objects.equals(friendship.getSender().getId(), ownerId)) {
            inbox.setSenderLastSeen(message);
        } else if (Objects.equals(friendship.getReceiver().getId(), ownerId)) {
            inbox.setReceiverLastSeen(message);
        } else {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Not your inbox.");
        }

        // set inbox
        inboxRepository.save(inbox);

        // evict message group
        redisService.evictKeysByPrefix("messages", "group-" + inboxId);
    }

    private Page<DResponseMessage> mapToDTO(Page<Message> messagePage) {

        return messagePage.map(entity -> {

            DResponseMessage message = modelMapper.map(entity, DResponseMessage.class);

            // remove deleted content
            setDeletedContent(message);

            return message;
        });
    }

    public void setDeletedContent(DResponseMessage message) {

        // main message
        if (message.getStatus() == MessageStatusType.INACTIVE) {
            message.setResources(new ArrayList<>());
            message.setContent(null);
        }

        // children
        if (!message.getReplies().isEmpty())
            message.getReplies().forEach(this::setDeletedContent);

    }

}
