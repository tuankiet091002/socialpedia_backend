package com.java.java_proj.dto.request.forupdate;

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
public class URequestChannelMember {

    @NotNull(message = "Channel Permission is required")
    private PermissionAccessType channelPermission;

    @NotNull(message = "Message Permission is required")
    private PermissionAccessType messagePermission;

    @NotNull(message = "Member Permission is required")
    private PermissionAccessType memberPermission;
}
