package com.java.java_proj.services;

import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.entities.Resource;
import com.java.java_proj.entities.User;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ResourceRepository;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.entities.miscs.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    FirebaseFileService fileService;
    @Autowired
    ResourceRepository resourceRepository;

    private User getOwner() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? null :
                ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    @Override
    public DResponseResource findById(Integer id) {
        return resourceRepository.findOneById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Resource not found"));
    }

    @Override
    public Resource addFile(MultipartFile file) {

        User owner = getOwner();

        try {

            // save to cloud
            String generatedName = fileService.save(file);
            String imageUrl = fileService.getImageUrl(generatedName);

            // create entity
            Resource resource = Resource.builder()
                    .filename(file.getOriginalFilename())
                    .generatedName(generatedName)
                    .url(imageUrl)
                    .build();


            return resourceRepository.save(resource);

        } catch (Exception e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't save file");
        }
    }

    @Override
    public void deleteFile(Integer id) {
        try {
            Resource resource = resourceRepository.findById(id)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Training material not found."));

            // delete file on firebase
            fileService.delete(resource.getFilename());

            // delete entity
            resourceRepository.deleteById(id);
        } catch (IOException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete file.");
        }
    }
}