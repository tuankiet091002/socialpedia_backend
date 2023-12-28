package com.java.java_proj.entities;

import com.java.java_proj.entities.enums.PermissionAccessType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "user_permissions")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "channel_permission")
    @Enumerated(EnumType.STRING)
    private PermissionAccessType channelPermission;

    @Column(name = "user_permmission")
    @Enumerated(EnumType.STRING)
    private PermissionAccessType userPermission;

}
