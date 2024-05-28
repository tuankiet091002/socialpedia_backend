package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.forlist.LResponseNotification;
import com.java.java_proj.entities.Notification;
import com.java.java_proj.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Page<Notification> findByUserOrderByIdDesc(User user, Pageable pageable);

    @Modifying
    @Query("UPDATE Notification n SET n.type = 2 WHERE n.user = :user")
    void seenByUser(User user);

    @Modifying
    @Query("UPDATE Notification n SET n.type = 2 WHERE n.user = :user AND n.destination = :destination")
    void seenByUserAndDestination(User user, String destination);

    @Modifying
    @Query("UPDATE Notification n SET n.type = 2 WHERE n.destination = :destination")
    void seenByDestination(String destination);
}
