package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Integer> {

    @Query("SELECT m.channel FROM ChannelMember m " +
            "WHERE m.member = :user " +
            "AND m.channel.name LIKE CONCAT('%',:name, '%')")
    Page<Channel> findChannelIn(String name, User user, Pageable pageable);

    Optional<DResponseChannel> findOneById(Integer id);

    Integer countById(Integer id);
}

