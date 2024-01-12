package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.entities.enums.RequestType;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DResponseUserFriendship {

    private Integer senderId;
    private Integer receiverId;
    private RequestType status;
    private LocalDateTime modifiedDate;

}
