package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.entities.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    Optional<DResponseResource> findOneById(Integer id);

}
