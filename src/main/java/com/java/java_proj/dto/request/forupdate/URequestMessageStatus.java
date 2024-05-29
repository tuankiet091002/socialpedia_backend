package com.java.java_proj.dto.request.forupdate;

import com.java.java_proj.entities.enums.MessageStatusType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class URequestMessageStatus {

    @NotNull(message = "Message id is required.")
    private Integer id;

    @NotNull(message = "Message status is required.")
    private MessageStatusType status;
}