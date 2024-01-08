package com.java.java_proj.controllers;

import com.java.java_proj.entities.miscs.SocketMessage;
import com.java.java_proj.services.templates.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketMessageController {

    final private MessageService messageService;

    public SocketMessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @MessageMapping("/channel/{channelId}/typing")
    @SendTo("/channel/{channelId}")
    public SocketMessage typeInChannel(@DestinationVariable Integer channelId, SocketMessage message) {
        return new SocketMessage(SocketMessage.MessageType.TYPE, 0);
    }

    @MessageMapping("/channel/{channelId}/stopTyping")
    @SendTo("/channel/{channelId}")
    public SocketMessage stopTypeInChannel(@DestinationVariable Integer channelId, SocketMessage message) {
        return new SocketMessage(SocketMessage.MessageType.TYPE, 0);
    }

    @MessageMapping("/channel/{channelId}/see/{messageId}")
    public void seeChannelMessage(@DestinationVariable Integer channelId,
                                  @DestinationVariable Integer messageId,
                                  SocketMessage message) {
        messageService.seeChannelMessage(channelId, message.getOwner(), messageId);
    }

    @MessageMapping("/inbox/{inboxId}/see/{messageId}")
    public void seeInboxMessage(@DestinationVariable Integer inboxId,
                                @DestinationVariable Integer messageId,
                                SocketMessage message) {
        messageService.seeInboxMessage(inboxId, message.getOwner(), messageId);
    }
}
