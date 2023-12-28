package com.java.java_proj.repositories;

import com.java.java_proj.entities.Inbox;
import com.java.java_proj.entities.UserFriendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxRepository extends JpaRepository<Inbox, Integer> {

    Page<Inbox> findByNameContaining(String name, Pageable pageable);

    Integer countByFriendship(UserFriendship friendship);
}
