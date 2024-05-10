package com.java.java_proj.dto.response.fordetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DResponseResourceClass {

    private Integer id;

    private String filename;

    private String fileType;

    private Long fileSize ;

    private String url;
}
