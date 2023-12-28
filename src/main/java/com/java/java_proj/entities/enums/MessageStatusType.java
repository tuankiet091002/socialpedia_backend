package com.java.java_proj.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageStatusType {
    ACTIVE("active"),
    MODIFIED("modified"),
    INACTIVE("inactive"),
    PINNED("pinned");

    private final String value;
}
