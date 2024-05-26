package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DResponseInbox {

    private Integer id;

    private String name;

    private DResponseResource avatar;

    private LResponseUserMinimal modifiedBy;

    private LocalDateTime modifiedDate;

    private Integer lastSeenMessageId;
}
