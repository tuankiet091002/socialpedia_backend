package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.entities.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LResponseNotification {

    private Integer id;

    private DResponseResource avatar;

    private String title;

    private String content;

    private String destination;

    private NotificationType type;

    private LocalDateTime createdDate;
}

