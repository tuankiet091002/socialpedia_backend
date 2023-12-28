package com.java.java_proj.entities;


import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.entities.miscs.UserFriendshipCompositeKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_friendships")
@IdClass(UserFriendshipCompositeKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserFriendship {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestType status;

    @Column(name = "modified_date", columnDefinition = "DATETIME")
    private LocalDateTime modifiedDate;

}
