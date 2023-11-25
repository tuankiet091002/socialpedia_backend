package com.java.java_proj.dto.request.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RequestRefreshToken {

    @NotBlank(message = "Refresh token is required.")
    private String refreshToken;

}
