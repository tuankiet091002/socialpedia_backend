package com.java.java_proj.dto.request.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestRefreshToken {

    @NotBlank(message = "Refresh token is required.")
    private String refreshToken;

}
