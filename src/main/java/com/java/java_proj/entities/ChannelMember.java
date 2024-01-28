package com.java.java_proj.entities;


import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.entities.miscs.ChannelMemberCompositeKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "channel_members")
@IdClass(ChannelMemberCompositeKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChannelMember {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User member;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestType status;

    @Column(name = "message_permission")
    private PermissionAccessType messagePermission;

    @Column(name = "member_permission")
    private PermissionAccessType memberPermission;

    @ManyToOne
    @JoinColumn(name = "last_seen_message")
    private Message lastSeenMessage;

    @Column(name = "joined_date", columnDefinition = "DATE")
    private LocalDate joinedDate;

}
