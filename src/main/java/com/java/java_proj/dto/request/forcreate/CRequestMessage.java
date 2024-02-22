package com.java.java_proj.dto.request.forcreate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CRequestMessage {

    @NotBlank(message = "Message content is required.")
    private String content;

    private Integer replyTo;

    private List<MultipartFile> resourceFiles = new ArrayList<>();
}
