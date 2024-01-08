package com.java.java_proj.entities.miscs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocketMessage {
    public enum MessageType {
        TYPE,
        STOP_TYPE,
        CHAT,
        JOIN,
        LEAVE,
        SEEN,
    }

    private MessageType type;
    private Integer owner;

}
