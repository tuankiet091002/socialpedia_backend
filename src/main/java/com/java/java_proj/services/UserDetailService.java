package com.java.java_proj.services;

import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.ChannelMember;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.miscs.CustomUserDetail;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ChannelMemberRepository;
import com.java.java_proj.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {

    final private UserRepository userRepository;
    final private ChannelMemberRepository channelMemberRepository;

    public UserDetailService(UserRepository userRepository, ChannelMemberRepository channelMemberRepository) {
        this.userRepository = userRepository;
        this.channelMemberRepository = channelMemberRepository;
    }

    @Override
    public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {

        // fetch data
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found."));
        List<LResponseUser> friends = userRepository.findFriends(user);
        List<ChannelMember> channels = channelMemberRepository.findByMember(user);

        return CustomUserDetail.builder()
                .user(user)
                .friends(friends)
                .channels(channels)
                .build();
    }
}

