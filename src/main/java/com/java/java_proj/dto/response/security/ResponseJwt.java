package com.java.java_proj.dto.response.security;

import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.entities.UserPermission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseJwt {
    private String token;

    private String refreshToken;

    private DResponseUser user;
}

