package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.enums.PermissionAccessType;

public interface DResponseChannelMember {

    public LResponseUser getMember();

    public PermissionAccessType getPermission();

}
