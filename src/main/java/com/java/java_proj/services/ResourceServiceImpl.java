package com.java.java_proj.services;

import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.entities.Resource;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ResourceRepository;
import com.java.java_proj.services.templates.ResourceService;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResourceServiceImpl implements ResourceService {

    final private FirebaseFileService fileService;
    final private ResourceRepository resourceRepository;
    final private ModelMapper modelMapper;

    public ResourceServiceImpl(FirebaseFileService fileService, ResourceRepository resourceRepository, ModelMapper modelMapper) {
        this.fileService = fileService;
        this.resourceRepository = resourceRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public DResponseResource findById(Integer id) {

        Resource resource = resourceRepository.findOneById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Resource not found"));

        return modelMapper.map(resource, DResponseResource.class);
    }

    @Override
    public Resource addFile(MultipartFile file) {

        try {
            // save to cloud
            String generatedName = fileService.save(file);
            String imageUrl = fileService.getImageUrl(generatedName);
            String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
            Long size = file.getSize();

            // create entity
            Resource resource = Resource.builder()
                    .filename(file.getOriginalFilename())
                    .generatedName(generatedName)
                    .fileType(fileType)
                    .fileSize(size)
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

            // delete entity
            resourceRepository.deleteById(id);

            // delete file on firebase
            fileService.delete(resource.getGeneratedName());
        } catch (IOException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete file.");
        }
    }
}