package com.java.java_proj.dto.request.forupdate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class URequestChannel {

    @NotBlank(message = "Channel name is required.")
    private String name;

    @NotBlank(message = "Channel name is required.")
    private String description;
}
