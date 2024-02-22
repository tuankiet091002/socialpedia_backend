package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;
import com.java.java_proj.entities.enums.MessageStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DResponseMessage {

    public Integer id;

    public String content;

    public List<DResponseResourceClass> resources;

    public List<DResponseMessage> replies;

    public LResponseUserMinimal createdBy;

    public LocalDateTime modifiedDate;

    public MessageStatusType status;

    public List<LResponseUserMinimal> seenBy;
}
