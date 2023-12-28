package com.java.java_proj.dto.request.forcreate;

import com.java.java_proj.entities.enums.PermissionAccessType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CRequestChannelMember {

    @NotNull(message = "Member is required.")
    private Integer memberId;

    @NotNull(message = "Permission toward chat is required.")
    private PermissionAccessType chatPermission;

    @NotNull(message = "Permission toward member is required.")
    private PermissionAccessType memberPermission;
}
