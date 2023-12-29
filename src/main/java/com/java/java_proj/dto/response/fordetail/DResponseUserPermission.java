package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.entities.enums.PermissionAccessType;

public interface DResponseUserPermission {

    public String getName();

    public PermissionAccessType getUserPermission();

    public PermissionAccessType getChannelPermission();
}
