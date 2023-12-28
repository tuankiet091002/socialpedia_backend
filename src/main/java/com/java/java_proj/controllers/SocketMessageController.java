package com.java.java_proj.controllers;

import com.java.java_proj.entities.miscs.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SocketMessageController {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/channel/greetings")
    public SocketMessage greeting(SocketMessage message) {
        return new SocketMessage("Hello, " + message.getContent());
    }

//    @MessageMapping("/channel/{channelId}")
//    @SendTo("/channel/{channelId}")
//    public SocketMessage channelFunction(SocketMessage message, @DestinationVariable Integer channelId) {
//        return new SocketMessage("Hello, " + message.getContent());
//    }
}
