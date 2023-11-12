package com.java.java_proj.entities.miscs;

import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.User;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ChannelMemberCompositeKey implements Serializable {

    private Channel channel;
    private User member;
}
