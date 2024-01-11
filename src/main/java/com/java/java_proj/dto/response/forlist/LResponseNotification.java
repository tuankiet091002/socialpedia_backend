package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.entities.enums.NotificationType;

import java.time.LocalDateTime;

public interface LResponseNotification {

    public Integer getId();

    public DResponseResource getAvatar();

    public String getTitle();

    public String getContent();

    public String getTarget();

    public NotificationType getType();

    public LocalDateTime getCreatedDate();
}

