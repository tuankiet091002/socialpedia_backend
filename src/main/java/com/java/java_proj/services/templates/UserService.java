package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUserPassword;
import com.java.java_proj.dto.request.forupdate.URequestUserProfile;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.request.security.RequestRefreshToken;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.dto.response.security.ResponseJwt;
import com.java.java_proj.dto.response.security.ResponseRefreshToken;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserFriendship;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User getOwner();

    Page<LResponseUser> getUserList(String name, Integer page, Integer size,
                                    String orderBy, String orderDirection);

    Page<LResponseUser> getFriendList(String name, Integer page, Integer size,
                                      String orderBy, String orderDirection);

    public DResponseUser getUserProfile(String userEmail);

    public void register(CRequestUser requestUser);

    public ResponseJwt login(RequestLogin requestLogin);

    public ResponseRefreshToken refreshToken(RequestRefreshToken requestToken);

    public void updateUserProfile(URequestUserProfile requestUser);

    public void updateUserRole(Integer userId, String role);

    public void updateUserPassword(URequestUserPassword requestUserPassword);

    public void updateUserAvatar(MultipartFile file);

    public void disableUser(Integer userId);

    public void createFriendRequest(Integer userId);

    public void acceptFriendRequest(Integer userId);

    public void rejectFriendRequest(Integer userId);

    public void unFriend(Integer userId);

    public UserFriendship findFriendship(Integer userId);

}
