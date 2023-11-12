package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.request.forcreate.CRequestUser;

import java.time.LocalDate;
import java.util.List;

public interface DResponseMessage {

    public Integer getId();

    public String getContent();

    public List<DResponseResource> getResources();

    public CRequestUser getCreatedBy();

    public LocalDate getCreatedDate();

    public CRequestUser getModifiedBy();

    public LocalDate getModifiedDate();
}
