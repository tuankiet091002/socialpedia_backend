package com.java.java_proj.controllers;

import com.java.java_proj.entities.miscs.SocketMessage;
import com.java.java_proj.services.templates.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketMessageController {

    private final MessageService messageService;

    public SocketMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/space/{locationId}")
    @SendTo("/space/{locationId}")
    public SocketMessage typeInChannel(@DestinationVariable Integer locationId,
                                       SocketMessage message) {
        if (message.getType() == SocketMessage.MessageType.SEEN)
            messageService.seeInboxMessage(locationId, message.getOwner().getId());
        return message;
    }

}
