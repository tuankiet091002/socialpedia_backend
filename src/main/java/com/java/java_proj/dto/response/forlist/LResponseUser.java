package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.dto.response.fordetail.DResponseResource;

import java.time.LocalDate;

public interface LResponseUser {
    public Integer getId();

    public String getName();

    public String getEmail();

    public String getPhone();

    public LocalDate getDob();

    public LResponseUserPermission getRole();

    public Boolean getGender();

    public DResponseResource getAvatar();
}
