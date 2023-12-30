package com.java.java_proj.entities.miscs;

import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.ChannelMember;
import com.java.java_proj.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomUserDetail implements UserDetails {

    private User user;
    private List<LResponseUser> friends;
    private List<ChannelMember> channels;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        // global privilege
        authorities.add(new SimpleGrantedAuthority("GLOBAL_USER_" + user.getRole().getUserPermission().toString()));
        authorities.add(new SimpleGrantedAuthority("GLOBAL_CHANNEL_" + user.getRole().getChannelPermission().toString()));

        // inbox privilege
        friends.forEach(friend -> {
            authorities.add(new SimpleGrantedAuthority(friend.getId() + "_USER_SELF"));
        });

        // channel privilege
        channels.forEach(channel -> {
            authorities.add(new SimpleGrantedAuthority(channel.getChannel().getId() + "_MEMBER_" + channel.getMemberPermission().toString()));
            authorities.add(new SimpleGrantedAuthority(channel.getChannel().getId() + "_MESSAGE_" + channel.getMessagePermission().toString()));
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


