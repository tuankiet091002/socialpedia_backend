package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.entities.enums.PermissionAccessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DResponseUserPermissionClass {

    public String name;

    public PermissionAccessType userPermission;

    public PermissionAccessType channelPermission;
}
