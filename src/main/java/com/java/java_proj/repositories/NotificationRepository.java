package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.forlist.LResponseNotification;
import com.java.java_proj.entities.Notification;
import com.java.java_proj.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Page<LResponseNotification> findByUserOrderByIdDesc(User user, Pageable pageable);
}
