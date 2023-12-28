package com.java.java_proj.dto.request.forupdate;

import com.java.java_proj.entities.enums.PermissionAccessType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class URequestChannelMember {

    @NotBlank(message = "Message Permission is required")
    private PermissionAccessType messagePermission;

    @Column(name = "Member Permission is required")
    private PermissionAccessType memberPermission;
}
