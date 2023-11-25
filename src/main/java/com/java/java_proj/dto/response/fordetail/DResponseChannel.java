package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUser;

import java.time.LocalDate;
import java.util.List;

public interface DResponseChannel {

    public Integer getId();

    public String getName();

    public String getDescription();

    public DResponseResource getAvatar();

    public List<DResponseChannelMember> getChannelMembers();

    public LResponseUser getCreatedBy();

    public LocalDate getCreatedDate();

    public LResponseUser getModifiedBy();

    public LocalDate getModifiedDate();

}
