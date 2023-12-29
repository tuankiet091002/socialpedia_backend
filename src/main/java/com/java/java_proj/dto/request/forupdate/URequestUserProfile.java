package com.java.java_proj.dto.request.forupdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class URequestUserProfile {

    @NotBlank(message = "User name is required.")
    private String name;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^0\\d{9}")
    private String phone;

    @NotBlank(message = "Date of birth is required.")
    private String dob;

    private Boolean gender;
}
