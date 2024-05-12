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
@Table(name = "channels")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ChatSpaceSequence")
    @SequenceGenerator(name = "ChatSpaceSequence", sequenceName = "space_seq")
    private Integer id;

    @Column(name = "name")
    @Convert(converter = AttributeEncryptor.class)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @Convert(converter = AttributeEncryptor.class)
    private String description;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private Boolean isActive;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avatar")
    private Resource avatar;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "channel", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChannelMember> channelMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "channel_messages",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id"))
    @OrderBy(value = "id DESC")
    private List<Message> messages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_date", columnDefinition = "DATETIME")
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @Column(name = "modified_date", columnDefinition = "DATETIME")
    private LocalDateTime modifiedDate;

}
