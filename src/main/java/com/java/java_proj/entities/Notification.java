package com.java.java_proj.entities;


import com.java.java_proj.entities.enums.NotificationType;
import com.java.java_proj.util.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "avatar")
    private Resource avatar;

    @Column(name = "title")
    @Convert(converter = AttributeEncryptor.class)
    private String title;

    @Column(name = "content")
    @Convert(converter = AttributeEncryptor.class)
    private String content;

    @Column(name = "destination")
    @Convert(converter = AttributeEncryptor.class)
    private String destination;

    @Column(name = "type")
    private NotificationType type;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

}
