package com.java.java_proj.repositories;

import com.java.java_proj.entities.Inbox;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserFriendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InboxRepository extends JpaRepository<Inbox, Integer> {

    // search by inbox name and opponent name
    @Query("SELECT i FROM Inbox i WHERE i.friendship IN " +
            "(SELECT f FROM UserFriendship f WHERE f.status = 'ACCEPTED' AND " +
            "f.sender = :user OR f.receiver = :user) " +
            "AND ((i.friendship.sender = :user AND CONCAT(i.name, ' ' ,i.friendship.receiver.name) LIKE CONCAT('%', :name, '%')) " +
            "OR (i.friendship.receiver = :user AND CONCAT(i.name, ' ' ,i.friendship.sender.name) LIKE CONCAT('%', :name, '%')))" +
            "AND i.isActive = TRUE")
    Page<Inbox> findByNameAndUser(String name, User user, Pageable pageable);

    @Query("SELECT i.id FROM Inbox i INNER JOIN UserFriendship f ON i.friendship = f WHERE f.status = 'ACCEPTED' AND f.sender = :user OR f.receiver = :user AND i.isActive = TRUE")
    List<Integer> findInboxIdList(User user);

    Optional<Inbox> findByFriendshipAndIsActive(UserFriendship friendship, Boolean status);

    Integer countByFriendshipAndIsActive(UserFriendship friendship, Boolean status);
}
