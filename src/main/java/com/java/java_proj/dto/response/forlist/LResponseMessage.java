package com.java.java_proj.dto.response.forlist;

import java.time.LocalDate;

public interface LResponseMessage {

    public Integer getId();

    public String getContent();

    public LocalDate getCreatedDate();

    public LocalDate getModifiedDate();

}
