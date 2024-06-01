package com.java.java_proj.repositories;

import com.java.java_proj.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Integer countByEmail(String email);

    Integer countByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.name LIKE CONCAT('%',:name, '%') AND u.isActive = true ")
    Page<User> findByNameContaining(String name, Pageable paging);

    // only return if user type correct word
    @Query("SELECT u FROM User u " +
            "WHERE (u.name LIKE CONCAT(:name, ' %') OR " +
            "(u.name LIKE CONCAT('% ', :name)) OR " +
            "(u.name LIKE CONCAT('% ', :name, ' %')) OR" +
            "(u.name LIKE :name)) " +
            "AND u.isActive = true ")
    Page<User> findBySpecificWord(String name, Pageable paging);

    @Query(value = "SELECT u.id from User u " +
            "WHERE u IN (SELECT s.receiver FROM UserFriendship s WHERE s.sender = :user AND s.status = 'ACCEPTED')" +
            "OR u IN (SELECT r.sender FROM UserFriendship r WHERE r.receiver = :user AND r.status = 'ACCEPTED')")
    List<Integer> findFriendIdList(User user);

}

