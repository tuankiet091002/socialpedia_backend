package com.java.java_proj.dto.request.forupdate;

import com.java.java_proj.dto.request.forcreate.CRequestChannelMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class URequestChannel {

    @NotNull(message = "Channel id is required.")
    private Integer id;

    @NotBlank(message = "Channel name is required.")
    private String name;

    @NotBlank(message = "Channel name is required.")
    private String description;

    @NotEmpty(message = "At least one additional member beside you is required.")
    private List<CRequestChannelMember> channelMembersId;
}
