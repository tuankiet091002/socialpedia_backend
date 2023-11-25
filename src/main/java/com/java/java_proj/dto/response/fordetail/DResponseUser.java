package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.dto.response.forlist.LResponseUserPermission;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DResponseUser {
    public Integer getId() ;

    public String getName();

    public String getEmail();

    public String getPhone();

    public LocalDate getDob();

    public LResponseUserPermission getRole();

    public Boolean getGender();

    public Boolean getIsActive();

    public LocalDateTime getCreatedDate();

    public LocalDateTime getModifiedDate();

    public DResponseResource getAvatar();

    public List<LResponseUser> getFriends();
}
