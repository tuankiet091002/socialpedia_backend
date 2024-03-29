package com.java.java_proj.dto.response.forlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LResponseMessage {

    public Integer id;

    public String content;

    public LocalDateTime modifiedDate;

}