package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.dto.response.fordetail.DResponseResource;

public interface LResponseNotification {

    public Integer getId();

    public DResponseResource getAvatar();

    public String getTitle();

    public String getContent();

    public String getTarget();

    public Boolean getIsRead();
}

