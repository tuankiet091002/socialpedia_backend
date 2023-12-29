package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.forlist.LResponseUser;
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

    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.friends WHERE u.email = ?1")
    User findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    Integer countByEmail(String email);

    Integer countByPhone(String phone);

    Page<LResponseUser> findByNameContaining(String name, Pageable paging);

    @Query(value = "SELECT u from User u " +
            "WHERE u IN (SELECT s.receiver FROM UserFriendship s WHERE s.sender = :user AND s.status = 'ACCEPTED')" +
            "OR u IN (SELECT r.sender FROM UserFriendship r WHERE r.receiver = :user AND r.status = 'ACCEPTED')" +
            "AND u.name LIKE CONCAT('%',:name, '%')")
    Page<LResponseUser> findFriendsByName(String name, User user, Pageable paging);

    @Query(value = "SELECT u from User u " +
            "WHERE u IN (SELECT s.receiver FROM UserFriendship s WHERE s.sender = :user AND s.status = 'ACCEPTED')" +
            "OR u IN (SELECT r.sender FROM UserFriendship r WHERE r.receiver = :user AND r.status = 'ACCEPTED')")
    List<LResponseUser> findFriends(User user);

}

