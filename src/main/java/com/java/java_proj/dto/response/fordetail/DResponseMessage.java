package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUser;

import java.time.LocalDateTime;
import java.util.List;

public interface DResponseMessage {

    public Integer getId();

    public String getContent();

    public List<DResponseResource> getResources();

    public LResponseUser getCreatedBy();

    public LocalDateTime getCreatedDate();

    public LResponseUser getModifiedBy();

    public LocalDateTime getModifiedDate();
}
