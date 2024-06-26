package com.java.java_proj.dto.response.forlist;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.dto.response.fordetail.DResponseUserPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS,
        property = "@class"
)
public class LResponseUser {

    private Integer id;

    private String name;

    private DResponseResource avatar;

    private String phone;

    private LocalDate dob;

    private DResponseUserPermission role;

    private Boolean gender;
}
