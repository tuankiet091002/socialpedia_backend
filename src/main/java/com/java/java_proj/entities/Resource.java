package com.java.java_proj.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "resources")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "generated_name", nullable = false)
    private String generatedName;

    @Column(name = "url", nullable = false)
    private String url;
}
