package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.RequestType;

import java.time.LocalDate;

public interface DResponseChannelMember {

    public LResponseUserMinimal getMember();

    public RequestType getStatus();

    public PermissionAccessType getChannelPermission();

    public PermissionAccessType getMessagePermission();

    public PermissionAccessType getMemberPermission();

    public LocalDate getJoinedDate();

}
