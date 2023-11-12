package com.java.java_proj.dto.request.forcreate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CRequestMessage {

    @NotNull(message = "Channel id is required.")
    private Integer channelId;

    @NotBlank(message = "Message content is required.")
    private String content;

    private List<MultipartFile> resourceFiles = new ArrayList<>();
}
