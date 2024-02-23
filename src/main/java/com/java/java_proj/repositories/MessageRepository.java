package com.java.java_proj.repositories;

import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.Inbox;
import com.java.java_proj.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m " +
            "WHERE m IN (SELECT c.messages FROM Channel c WHERE c = :channel) " +
            "AND m.content LIKE CONCAT('%', :content, '%') AND m.replyTo IS null ")
    Page<Message> findByChannel(String content, Channel channel, Pageable pageable);

    @Query("SELECT m FROM Message m " +
            "WHERE m IN (SELECT i.messages FROM Inbox i WHERE i = :inbox) " +
            "AND m.content LIKE CONCAT('%', :content, '%') AND m.replyTo IS null ")
    Page<Message> findByInbox(String content, Inbox inbox, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Message m " +
            "WHERE m = :message AND " +
            "m IN (SELECT c.messages FROM Channel c WHERE c.id = :locationId) " +
            "OR m IN (SELECT i.messages FROM Inbox i WHERE i.id = :locationId)")
    Integer countByLocation(Message message, Integer locationId);

    @Query("SELECT m FROM Message m " +
            "WHERE m IN (SELECT c.messages FROM Channel c WHERE c = :channel) and m.id = :id")
    Optional<Message> findByIdAndChannel(Integer id, Channel channel);

    @Query("SELECT m FROM Message m " +
            "WHERE m IN (SELECT i.messages FROM Inbox i WHERE i = :inbox) and m.id = :id")
    Optional<Message> findByIdAndInbox(Integer id, Inbox inbox);

}
