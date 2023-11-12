package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestMessage;
import com.java.java_proj.dto.request.forupdate.URequestMessage;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.Message;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.services.templates.MessageService;
import com.java.java_proj.services.templates.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ResourceService resourceService;

    @Override
    public Page<DResponseMessage> getAllMessageByChannel(String content, Integer channelId, Integer page, Integer size) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));

        // create pageable
        Pageable pageable = PageRequest.of(page - 1, size);

        return messageRepository.findByContentContainingAndChannel(content, channel, pageable);
    }

    @Override
    public DResponseMessage createMessage(CRequestMessage requestMessage) {

        Channel channel = channelRepository.findById(requestMessage.getChannelId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));

        Message message = new Message();
        message.setChannel(channel);
        message.setContent(requestMessage.getContent());
        message.setCreatedBy(null);
        message.setCreatedDate(LocalDateTime.now());
        message.setResources(requestMessage.getResourceFiles().stream()
                .map(file -> resourceService.addFile(file))
                .collect(Collectors.toList()));

        message = messageRepository.save(message);

        return messageRepository.findOneById(message.getId());
    }

    @Override
    public DResponseMessage updateMessage(URequestMessage requestMessage) {

        Message message = messageRepository.findById(requestMessage.getId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Message not found."));

        message.setContent(requestMessage.getContent());
        message.setModifiedBy(null);
        message.setModifiedDate(LocalDateTime.now());
        message = messageRepository.save(message);

        return messageRepository.findOneById(message.getId());
    }

    @Override
    public void deleteMessage(Integer id) {

        if (messageRepository.countById(id) == 0)
            throw new HttpException(HttpStatus.NOT_FOUND, "Message not found");

        messageRepository.deleteById(id);
    }
}
