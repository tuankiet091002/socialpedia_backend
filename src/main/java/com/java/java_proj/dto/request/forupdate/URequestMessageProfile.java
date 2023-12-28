package com.java.java_proj.dto.request.forupdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class URequestMessageProfile {

    @NotNull(message = "Message id is required.")
    private Integer id;

    @NotBlank(message = "Message content is required.")
    private String content;
}