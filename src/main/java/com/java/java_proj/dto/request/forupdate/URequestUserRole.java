package com.java.java_proj.dto.request.forupdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class URequestUserRole {

    @NotNull(message = "User id is required")
    private Integer id;

    @NotBlank(message = "New role is required.")
    private String role;
}
