package com.java.java_proj.controllers;

import com.java.java_proj.entities.miscs.GreetingMessage;
import com.java.java_proj.entities.miscs.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.HtmlUtils;

@Controller
public class SocketMessageController {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/join/{channelId}")
    @SendTo("/channel/{channel}")
    public GreetingMessage greetToChannel(@DestinationVariable Integer channelId, HelloMessage message) {

        System.out.println("Channel id la " + channelId);
        return new GreetingMessage("Hello, " + HtmlUtils.htmlEscape(message.getName()) + " in " + channelId);
    }

    @MessageMapping("/leave/{channelId}")
    @SendTo("/channel/{channel}")
    public GreetingMessage byeToChannel(@PathVariable Integer channelId, HelloMessage message) {

        System.out.println("Channel id la " + channelId);
        return new GreetingMessage("See you again, " + HtmlUtils.htmlEscape(message.getName()) + " in " + channelId);
    }


}
