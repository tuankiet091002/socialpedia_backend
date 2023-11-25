package com.java.java_proj.repositories;

import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.ChannelMember;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.miscs.ChannelMemberCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelMemberRepository extends JpaRepository<ChannelMember, ChannelMemberCompositeKey> {

    Integer countByChannelAndMember(Channel channel, User user);
}
