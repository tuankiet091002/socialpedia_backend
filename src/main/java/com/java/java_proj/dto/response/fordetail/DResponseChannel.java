package com.java.java_proj.dto.response.fordetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class DResponseChannel {

    private Integer id;

    private String name;

    private String description;

    private DResponseResource avatar;

    private DResponseMessage latestMessage;

}
