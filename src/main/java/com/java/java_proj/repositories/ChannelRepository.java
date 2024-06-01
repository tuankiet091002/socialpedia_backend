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

    Page<Channel> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT m.channel FROM ChannelMember m " +
            "WHERE m.member = :user " +
            "AND m.status = 'ACCEPTED' " +
            "AND m.channel.name LIKE CONCAT('%',:name, '%') " +
            "AND m.channel.isActive = TRUE " +
            "ORDER BY m.channel.modifiedDate DESC ")
    Page<Channel> findPersonalChannelList(String name, User user, Pageable pageable);

    @Query("SELECT c FROM Channel c JOIN FETCH c.channelMembers WHERE c.id = :id AND c.isActive = TRUE")
    Optional<Channel> findOneById(Integer id);

}

