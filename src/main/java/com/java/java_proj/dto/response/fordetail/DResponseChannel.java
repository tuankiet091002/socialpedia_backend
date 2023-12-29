package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;

import java.time.LocalDateTime;
import java.util.List;

public interface DResponseChannel {

    public Integer getId();

    public String getName();

    public String getDescription();

    public DResponseResource getAvatar();

    public List<DResponseChannelMember> getChannelMembers();

    public LResponseUserMinimal getCreatedBy();

    public LocalDateTime getCreatedDate();

    public LResponseUserMinimal getModifiedBy();

    public LocalDateTime getModifiedDate();

}
