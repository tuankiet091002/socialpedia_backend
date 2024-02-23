package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.dto.response.fordetail.DResponseResourceClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LResponseChatSpace {

    private Integer id;

    private String name;

    private DResponseResourceClass avatar;

    private Integer memberNum;

    private LResponseMessage latestMessage;

    private LResponseUserMinimalClass createdBy;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Boolean isActive;
}
