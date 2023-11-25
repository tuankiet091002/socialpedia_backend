package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUser;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.User;
import org.springframework.data.domain.Page;

public interface UserService {

    User getOwner();

    Page<LResponseUser> getAllUser(Integer id, String name, String email,
                                   String orderBy, Integer page, Integer size, String orderDirection);

    public DResponseUser createUser(CRequestUser user);

    public DResponseUser updateUser(URequestUser user);

    public DResponseUser updateUserRole(Integer id, String role);

    public DResponseUser verifyUser(RequestLogin requestLogin);

    public DResponseUser addFriend(Integer user);

    public DResponseUser deleteFriend(Integer user);
}
