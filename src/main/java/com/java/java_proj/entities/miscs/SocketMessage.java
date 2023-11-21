package com.java.java_proj.entities.miscs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocketMessage {
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    private MessageType type;
    private String content;
    private String sender;

    public SocketMessage(String content) {
        this.type = MessageType.CHAT;
        this.content = content;
        this.sender = "";
    }
}
