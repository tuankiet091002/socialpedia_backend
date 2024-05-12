package com.java.java_proj.entities;


import com.java.java_proj.util.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inboxs")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Inbox {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ChatSpaceSequence")
    private Integer id;

    @Column(name = "name")
    @Convert(converter = AttributeEncryptor.class)
    private String name;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "sender_id", referencedColumnName = "sender_id"),
            @JoinColumn(name = "receiver_id", referencedColumnName = "receiver_id")
    })
    private UserFriendship friendship;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "sender_last_seen")
    private Message senderLastSeen;

    @ManyToOne
    @JoinColumn(name = "receiver_last_seen")
    private Message receiverLastSeen;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "inbox_messages",
            joinColumns = @JoinColumn(name = "inbox_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id"))
    @OrderBy(value = "id DESC")
    private List<Message> messages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @Column(name = "modified_date", columnDefinition = "DATETIME")
    private LocalDateTime modifiedDate;
}
