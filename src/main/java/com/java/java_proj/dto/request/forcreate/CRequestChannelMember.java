package com.java.java_proj.dto.request.forcreate;

import com.java.java_proj.entities.enums.PermissionAccessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CRequestChannelMember {

    @NotNull(message = "Member is required.")
    private Integer memberId;

    @NotNull(message = "Permission access type is required.")
    private PermissionAccessType permission;
}
