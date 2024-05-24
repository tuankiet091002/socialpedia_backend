package com.java.java_proj.controllers;

import com.java.java_proj.entities.miscs.SocketMessage;
import com.java.java_proj.services.templates.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketMessageController {

    @MessageMapping("/space/{locationId}")
    @SendTo("/space/{locationId}")
    public SocketMessage typeInChannel(@DestinationVariable Integer locationId,
                                       SocketMessage message) {

        return message;
    }

}
