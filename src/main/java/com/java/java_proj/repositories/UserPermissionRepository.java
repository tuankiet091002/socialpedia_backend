package com.java.java_proj.repositories;

import com.java.java_proj.entities.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Integer> {

    UserPermission findByName(String name);
}
