package com.java.java_proj.dto.request.forcreate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CRequestChannel {

    @NotBlank(message = "Channel name is required.")
    private String name;

    @NotBlank(message = "Channel name is required.")
    private String description;

    private MultipartFile avatarFile;

    @NotEmpty(message = "At least one additional member beside you is required.")
    private List<CRequestChannelMember> channelMembersId;
}
