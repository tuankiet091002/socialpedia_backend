package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.dto.response.fordetail.DResponseResource;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LResponseChannel {

    private Integer id;

    private String name;

    private DResponseResource avatar;

    private DResponseMessage latestMessage;
}
