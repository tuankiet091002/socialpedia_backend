package com.java.java_proj.repositories;

import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserFriendship;
import com.java.java_proj.entities.miscs.UserFriendshipCompositeKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserFriendshipRepository extends JpaRepository<UserFriendship, UserFriendshipCompositeKey> {

    @Query(value = "SELECT u FROM UserFriendship u " +
            "WHERE ((u.sender = :user AND u.receiver.name LIKE CONCAT('%',:name, '%')) AND u.receiver.isActive = TRUE " +
            "OR ((u.receiver = :user AND u.sender.name LIKE CONCAT('%',:name, '%')))) AND u.sender.isActive = true " +
            "AND u.status = 'ACCEPTED'")
    Page<UserFriendship> findFriendsByName(String name, User user, Pageable paging);

    @Query("SELECT f FROM UserFriendship f " +
            "WHERE (f.sender = :user1 AND f.receiver = :user2) " +
            "OR (f.receiver = :user1 AND f.sender = :user2)")
    Optional<UserFriendship> findByUser(User user1, User user2);
}
