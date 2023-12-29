package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestMessage;
import com.java.java_proj.dto.request.forupdate.URequestMessageProfile;
import com.java.java_proj.dto.request.forupdate.URequestMessageStatus;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;
import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.Inbox;
import com.java.java_proj.entities.Message;
import com.java.java_proj.entities.Resource;
import com.java.java_proj.entities.enums.MessageStatusType;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.services.templates.MessageService;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    final private MessageRepository messageRepository;
    final private ChannelRepository channelRepository;
    final private InboxRepository inboxRepository;
    final private ResourceService resourceService;
    final private UserService userService;
    final private ModelMapper modelMapper;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, ChannelRepository channelRepository, InboxRepository inboxRepository, ResourceService resourceService, UserService userService, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.inboxRepository = inboxRepository;
        this.resourceService = resourceService;
        this.userService = userService;
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
        Message message = Message.builder()
                .content(requestMessage.getContent())
                .resources(requestMessage.getResourceFiles().stream()
                        .map(resourceService::addFile)
                        .collect(Collectors.toList()))
                .createdBy(userService.getOwner())
                .modifiedDate(LocalDateTime.now())
                .status(MessageStatusType.ACTIVE)
                .build();
        channel.getMessages().add(message);

        channelRepository.save(channel);
    }

    @Override
    @Transactional
    public void sendMessageToInbox(Integer locationId, CRequestMessage requestMessage) {

        // get inbox
        Inbox inbox = inboxRepository.findById(locationId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Inbox not found."));

        // build message
        Message message = Message.builder()
                .content(requestMessage.getContent())
                .resources(requestMessage.getResourceFiles().stream()
                        .map(resourceService::addFile)
                        .collect(Collectors.toList()))
                .createdBy(userService.getOwner())
                .modifiedDate(LocalDateTime.now())
                .status(MessageStatusType.ACTIVE)
                .build();
        inbox.getMessages().add(message);

        inboxRepository.save(inbox);
    }

    @Override
    public void updateMessageContent(Integer locationId, URequestMessageProfile requestMessage) {

        Message message = messageRepository.findById(requestMessage.getId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Message not found."));

        // check if message belong to that location
        if (!Objects.equals(message.getCreatedBy().getId(), userService.getOwner().getId()))
            throw new HttpException(HttpStatus.FORBIDDEN, "Can't change others message");

        message.setContent(requestMessage.getContent());
        message.setModifiedDate(LocalDateTime.now());

        messageRepository.save(message);
    }

    @Override
    public void updateMessageStatus(Integer locationId, URequestMessageStatus requestMessage) {

        // check and set
        Message message = messageRepository.findById(requestMessage.getId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Message not found."));

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

    private Page<DResponseMessage> mapToDTO(Page<Message> messagePage) {

        return messagePage.map(entity -> {

            // skip resource list to bypass model mapper exception
            List<Resource> resourceList = entity.getResources();
            entity.setResources(null);
            DResponseMessage message = modelMapper.map(entity, DResponseMessage.class);

            // convert missing fields
            ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
            message.setResources(resourceList.stream().map(x -> pf.createProjection(DResponseResource.class, x)).toList());
            message.setCreatedBy(pf.createProjection(LResponseUserMinimal.class, entity.getCreatedBy()));

            return message;
        });
    }
}
