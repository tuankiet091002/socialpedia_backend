package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;
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
public class DResponseChannel {

    private Integer id;

    private String name;

    private String description;

    private DResponseResource avatar;

    private List<DResponseChannelMember> channelMembers;

    private LResponseUserMinimal createdBy;

    private LocalDateTime createdDate;

    private LResponseUserMinimal modifiedBy;

    private LocalDateTime modifiedDate;

}
