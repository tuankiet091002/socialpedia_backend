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

    public Integer id;

    public String filename;

    public String fileType;

    public String url;
}
