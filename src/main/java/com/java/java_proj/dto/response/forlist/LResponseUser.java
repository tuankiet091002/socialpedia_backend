package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.dto.response.fordetail.DResponseUserPermission;

import java.time.LocalDate;

public interface LResponseUser {

    public Integer getId();

    public String getName();

    public String getEmail();

    public DResponseResource getAvatar();

    public String getPhone();

    public LocalDate getDob();

    public DResponseUserPermission getRole();

    public Boolean getGender();
}
