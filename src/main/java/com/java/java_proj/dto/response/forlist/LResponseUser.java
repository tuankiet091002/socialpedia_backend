package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.dto.response.fordetail.DResponseResource;

public interface LResponseUser {
    public Integer getId();

    public String getName();

    public String getEmail();

    public DResponseResource getAvatar();
}
