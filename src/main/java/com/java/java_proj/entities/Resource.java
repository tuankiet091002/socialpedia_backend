package com.java.java_proj.entities;

import com.java.java_proj.util.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;


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
    @Convert(converter = AttributeEncryptor.class)
    private String filename;

    @Column(name = "file_type", nullable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    @ColumnDefault("0")
    private Long fileSize;

    @Column(name = "generated_name", nullable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String generatedName;

    @Column(name = "url", nullable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String url;
}
