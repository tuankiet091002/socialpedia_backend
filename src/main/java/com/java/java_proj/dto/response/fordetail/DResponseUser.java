package com.java.java_proj.dto.response.fordetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DResponseUser {

    public Integer id;

    public String name;

    public String email;

    public String phone;

    public LocalDate dob;

    public DResponseUserPermission role;

    public Boolean gender;

    public DResponseResource avatar;

    public LocalDateTime modifiedDate;
}
