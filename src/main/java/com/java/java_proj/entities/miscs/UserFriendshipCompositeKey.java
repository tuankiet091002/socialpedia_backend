package com.java.java_proj.entities.miscs;

import com.java.java_proj.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFriendshipCompositeKey implements Serializable {

    private User sender;
    private User receiver;
}
