package com.java.java_proj.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "generated_name", nullable = false)
    private String generatedName;

    @Column(name = "url", nullable = false)
    private String url;
}
