package com.java.java_proj.entities;

import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.miscs.ChannelMemberCompositeKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@IdClass(ChannelMemberCompositeKey.class)
@Table(name = "channel_members")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChannelMember {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User member;

    @Column(name = "chat_permission")
    private PermissionAccessType chatPermission = PermissionAccessType.CREATE;

    @Column(name = "member_permission")
    private PermissionAccessType memberPermission = PermissionAccessType.VIEW;

    public ChannelMember(Channel channel, User member) {
        this.channel = channel;
        this.member = member;
    }
}
