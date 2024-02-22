package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestMessage;
import com.java.java_proj.dto.request.forupdate.URequestMessageProfile;
import com.java.java_proj.dto.request.forupdate.URequestMessageStatus;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;
import com.java.java_proj.entities.*;
import com.java.java_proj.entities.enums.MessageStatusType;
import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ChannelMemberRepository;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.services.templates.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    final private MessageRepository messageRepository;
    final private ChannelRepository channelRepository;
    final private ChannelMemberRepository channelMemberRepository;
    final private InboxRepository inboxRepository;
    final private ResourceService resourceService;
    final private UserService userService;
    final private NotificationService notificationService;
    final private ChannelService channelService;
    final private ModelMapper modelMapper;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, ChannelRepository channelRepository, ChannelMemberRepository channelMemberRepository, InboxRepository inboxRepository, ResourceService resourceService, UserService userService, NotificationService notificationService, ChannelService channelService, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.inboxRepository = inboxRepository;
        this.resourceService = resourceService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.channelService = channelService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Page<DResponseMessage> getMessagesFromChannel(Integer channelId, String content, Integer page, Integer size) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));

        // create pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // fetch entity page
        Page<Message> messagePage = messageRepository.findByChannel(content, channel, pageable);

        return mapToDTO(messagePage);
    }

    @Override
    @Transactional
    public Page<DResponseMessage> getMessagesFromInbox(Integer inboxId, String content, Integer page, Integer size) {

        Inbox inbox = inboxRepository.findById(inboxId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Inbox not found."));

        // create pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // fetch entity page
        Page<Message> messagePage = messageRepository.findByInbox(content, inbox, pageable);

        return mapToDTO(messagePage);
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

        channelRepository.save(channel);

        // create notification and send socket message
        notificationService.messageToChannel(locationId);
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

        inboxRepository.save(inbox);

        // create notification and send socket message
        notificationService.messageToInbox(locationId);
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
    }

    @Override
    @Transactional
    public void seeChannelMessage(Integer channelId, Integer messageId, Integer ownerId) {

        // check message
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Message not found."));

        // check if message belong to that location
        if (messageRepository.countByLocation(message, channelId) == 0)
            throw new HttpException(HttpStatus.FORBIDDEN, "Message don't belong to that location.");

        // check channel and member
        ChannelMember channelMember = channelService.findMemberRequest(channelId, ownerId);
        if (channelMember.getStatus() != RequestType.ACCEPTED)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Membership is invalid.");

        channelMemberRepository.save(channelMember);
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
    }

    private Page<DResponseMessage> mapToDTO(Page<Message> messagePage) {

        return messagePage.map(entity -> {

            DResponseMessage message = modelMapper.map(entity, DResponseMessage.class);

            // remove deleted content
            if (message.getStatus() == MessageStatusType.INACTIVE) {
                message.setResources(new ArrayList<>());
                message.setContent(null);
            }

            // convert missing fields
            ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
            message.setCreatedBy(pf.createProjection(LResponseUserMinimal.class, entity.getCreatedBy()));


            return message;
        });
    }
}
