package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DResponseChannelMember {

    private LResponseUserMinimal member;

    private RequestType status;

    private PermissionAccessType channelPermission;

    private PermissionAccessType messagePermission;

    private PermissionAccessType memberPermission;

    private LocalDate joinedDate;

}
