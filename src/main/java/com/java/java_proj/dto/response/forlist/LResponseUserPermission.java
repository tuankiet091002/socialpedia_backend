package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.entities.enums.PermissionAccessType;

public interface LResponseUserPermission {

    public String getName();

    public PermissionAccessType getUserPermission();

    public PermissionAccessType getChannelPermission();
}
