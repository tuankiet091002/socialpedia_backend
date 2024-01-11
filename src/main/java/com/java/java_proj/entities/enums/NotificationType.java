package com.java.java_proj.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {
    VIEW(0),
    REQUEST(1),
    DONE(2);

    private final Integer value;
}
