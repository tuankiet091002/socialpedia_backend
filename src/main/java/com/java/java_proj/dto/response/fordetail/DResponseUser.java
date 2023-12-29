package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


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

    public Boolean Gender;

    public DResponseResource avatar;

    public List<LResponseUser> friends;

    public LocalDateTime modifiedDate;
}
