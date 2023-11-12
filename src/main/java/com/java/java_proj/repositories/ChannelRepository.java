package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.dto.response.forlist.LResponseChannel;
import com.java.java_proj.entities.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Integer> {

    Page<LResponseChannel> findByNameContaining(String name, Pageable paging);

    Optional<DResponseChannel> findOneById(Integer id);

    Integer countById(Integer id);
}

