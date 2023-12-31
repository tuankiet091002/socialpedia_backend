package com.java.java_proj.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "target")
    private String target;

    @Column(name = "is_read")
    private Boolean isRead;

}
