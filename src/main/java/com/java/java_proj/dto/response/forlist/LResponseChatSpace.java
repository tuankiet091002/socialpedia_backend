package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.dto.response.fordetail.DResponseResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LResponseChatSpace {

    private Integer id;

    private String name;

    private DResponseResource avatar;

    private DResponseMessage latestMessage;
}
