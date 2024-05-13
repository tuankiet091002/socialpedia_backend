package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseChannelMember;
import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.ChannelMember;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.miscs.ChannelMemberCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelMemberRepository extends JpaRepository<ChannelMember, ChannelMemberCompositeKey> {

    Optional<ChannelMember> findByChannelAndMember(Channel channel, User user);

    Integer countByChannel(Channel channel);

    List<ChannelMember> findByMember(User user);
}
