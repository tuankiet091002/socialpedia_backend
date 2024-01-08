package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;
import com.java.java_proj.entities.enums.PermissionAccessType;

public interface DResponseChannelMember {

    public LResponseUserMinimal getMember();

    public PermissionAccessType getMessagePermission();

    public PermissionAccessType getMemberPermission();

}
