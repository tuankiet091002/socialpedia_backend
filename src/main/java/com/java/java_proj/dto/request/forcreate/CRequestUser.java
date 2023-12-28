package com.java.java_proj.dto.request.forcreate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class CRequestUser {

    @NotBlank(message = "User name is required.")
    private String name;

    @NotBlank(message = "Email address is required.")
    @Email(message = "Email address is invalid. Please check and input again.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^0\\d{9}", message = "Phone is invalid. Please check and input again.")
    private String phone;

    @NotBlank(message = "Date of birth is required.")
    private String dob;

    @NotBlank(message = "User type is required")
    private String role;

    @NotNull(message = "Gender is required")
    private Boolean gender;

    private MultipartFile avatarFile;
}
