package com.java.java_proj.services.templates;

import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.entities.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    DResponseResource findById(Integer id);

    Resource addFile(MultipartFile file);

    void deleteFile(Integer id);
}

