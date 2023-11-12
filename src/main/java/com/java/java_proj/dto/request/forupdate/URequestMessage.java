package com.java.java_proj.dto.request.forupdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class URequestMessage {

    @NotNull(message = "Message id is required.")
    private Integer id;

    @NotBlank(message = "Message content is required.")
    private String content;
}