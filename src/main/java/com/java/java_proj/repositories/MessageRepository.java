package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    Page<DResponseMessage> findByContentContainingAndChannel( String content, Channel channel, Pageable pageable);

    DResponseMessage findOneById(Integer id);

    DResponseMessage findTopByChannelOrderByCreatedDateDesc(Channel channel);

    Integer countById(Integer id);
}
