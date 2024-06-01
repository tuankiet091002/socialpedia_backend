package com.java.java_proj;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUserPassword;
import com.java.java_proj.dto.request.forupdate.URequestUserProfile;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.request.security.RequestRefreshToken;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.fordetail.DResponseUserFriendship;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.*;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.UserFriendshipRepository;
import com.java.java_proj.repositories.UserPermissionRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.UserServiceImpl;
import com.java.java_proj.services.templates.NotificationService;
import com.java.java_proj.services.templates.RefreshTokenService;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.util.DateFormatter;
import com.java.java_proj.util.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Spy
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPermissionRepository userPermissionRepository;
    @Mock
    private UserFriendshipRepository userFriendshipRepository;
    @Mock
    private InboxRepository inboxRepository;
    @Mock
    private ResourceService resourceService;
    @Spy
    private JwtTokenProvider tokenProvider;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private NotificationService notificationService;
    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Spy
    private ModelMapper modelMapper;
    @Spy
    private DateFormatter dateFormatter;

    @BeforeEach
    public void setUserService() {
        Mockito.doReturn(User.builder().id(0).password("123456").build()).when(userService).getOwner();
    }

    @BeforeEach
    public void setUserRepository() {

        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setEmail("kiet" + i + 1 + "@gmail.com");
            user.setPassword("123456");

            users.add(user);
        }

        Mockito.when(userRepository.findByNameContaining(any(String.class), any(Pageable.class))).thenReturn(new PageImpl<>(users, pageable, users.size()));
        Mockito.when(userRepository.findById(eq(0))).thenReturn(Optional.ofNullable(users.get(0)));
        Mockito.when(userRepository.countByEmail(any(String.class))).thenReturn(0);
        Mockito.when(userRepository.countByPhone(any(String.class))).thenReturn(0);
        Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(users.get(0)));
    }

    @BeforeEach
    public void setUserPermissionRepository() {

        UserPermission admin = UserPermission.builder()
                .name("admin")
                .userPermission(PermissionAccessType.MODIFY)
                .channelPermission(PermissionAccessType.MODIFY)
                .build();
        // user
        UserPermission user = UserPermission.builder()
                .name("user")
                .userPermission(PermissionAccessType.SELF)
                .channelPermission(PermissionAccessType.SELF)
                .build();

        Mockito.when(userPermissionRepository.findByName(eq("admin")))
                .thenReturn(admin);

        Mockito.when(userPermissionRepository.findByName(eq("user")))
                .thenReturn(user);
    }

    @BeforeEach
    public void setUserFriendshipRepository() {

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId(i);
            user.setEmail("kiet" + i + 1 + "@gmail.com");

            users.add(user);
        }

        Mockito.when(userFriendshipRepository.findByUser(any(User.class), any(User.class)))
                .thenReturn(Optional.of(UserFriendship.builder().sender(users.get(0)).receiver(users.get(1)).status(RequestType.ACCEPTED).modifiedDate(LocalDateTime.now()).build()));
    }

    @BeforeEach
    public void setInboxRepository() {

        Mockito.when(inboxRepository.findByFriendshipAndIsActive(any(UserFriendship.class), eq(true)))
                .thenReturn(Optional.of(Inbox.builder().id(0).name("TESTING INBOX").isActive(true).build()));
    }

    @BeforeEach
    public void setResourceService() {

        Mockito.when(resourceService.addFile(any()))
                .thenReturn(Resource.builder().id(0).filename("filename").fileSize(100000L).fileType("pdf").url("https://drive.google.com").generatedName("iajhrcnalrhaneioubciwreuoawiernmawrcawopdf").build());

        Mockito.doNothing().when(resourceService).deleteFile(any(Integer.class));
    }

    @BeforeEach
    public void setRefreshTokenService() {

        Mockito.when(refreshTokenService.findActiveToken(any(String.class)))
                .thenReturn(RefreshToken.builder().id(0).token("token").expiryDate(LocalDate.now().plusDays(1)).build());

        Mockito.when(refreshTokenService.createToken(any(String.class)))
                .thenReturn(RefreshToken.builder().id(0).token("token").expiryDate(LocalDate.now().plusDays(1)).build());

        Mockito.doNothing().when(refreshTokenService).deActiveUserToken(any(Integer.class));
    }

    @BeforeEach
    public void setNotificationService() {
        Mockito.doNothing().when(notificationService).friendRequestSend(any(User.class), any(User.class));

        Mockito.doNothing().when(notificationService).friendRequestAccepted(any(User.class), any(User.class));

        Mockito.doNothing().when(notificationService).seenByUserAndDestination(any(User.class), any(String.class));
    }

    ///////////////////////////////////////////////////////////////////
    ////////////////////////// TESTING PHASE //////////////////////////
    ///////////////////////////////////////////////////////////////////
    @Test
    public void testGetUserList() {

        Page<LResponseUser> userPage = userService.getFullUserList("", 0, 10, "dob", "DESC");

        Assertions.assertEquals(10, userPage.getContent().size());
        Mockito.verify(userRepository, Mockito.times(1)).findByNameContaining(any(String.class), any(Pageable.class));
        Mockito.verify(modelMapper, Mockito.times(10)).map(any(User.class), any());
    }

//    @Test
//    public void testGetFriendList() {
//
//        Page<LResponseUserMinimal> userPage = userService.getFriendList("", 0, 10);
//
//        Assertions.assertEquals(10, userPage.getContent().size());
//        Mockito.verify(userRepository, Mockito.times(1)).findFriendsByName(any(String.class), any(User.class), any(Pageable.class));
//    }

    @Test
    public void testGetUserprofile() {

        DResponseUser user = userService.getUserProfile(0);

        Assertions.assertEquals(0, user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).findById(eq(0));
    }

    @Test
    public void testGetUserFriendship() {

        Assertions.assertThrows(HttpException.class, () -> userService.getUserFriendship(11));

        DResponseUserFriendship friendship = userService.getUserFriendship(0);

        Assertions.assertEquals(RequestType.ACCEPTED, friendship.getStatus());
        Mockito.verify(userFriendshipRepository, Mockito.times(1)).findByUser(any(User.class), any(User.class));
    }

    @Test
    public void testRegister() {

        CRequestUser requestUser = new CRequestUser();
        requestUser.setName("Kiet");
        requestUser.setEmail("kiet10@gmail.com");
        requestUser.setPassword("123456");
        requestUser.setPhone("0123456789");
        requestUser.setDob("01/01/2024");
        requestUser.setGender(true);

        userService.register(requestUser);

        Mockito.verify(userRepository, Mockito.times(1)).countByEmail(eq("kiet10@gmail.com"));
        Mockito.verify(userRepository, Mockito.times(1)).countByPhone(eq("0123456789"));
        Mockito.verify(bCryptPasswordEncoder, Mockito.times(1)).encode(eq("123456"));
        Mockito.verify(dateFormatter, Mockito.times(1)).formatDate(eq("01/01/2024"));
        Mockito.verify(userPermissionRepository, Mockito.times(1)).findByName(eq("user"));
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void testLogin() {

        RequestLogin requestLogin = new RequestLogin();
        requestLogin.setEmail("kiet@gmail.com");
        requestLogin.setPassword("123456");

        Assertions.assertThrows(HttpException.class, () -> userService.login(requestLogin));

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(eq("kiet@gmail.com"));
        Mockito.verify(bCryptPasswordEncoder, Mockito.times(1)).matches(eq("123456"), any(String.class));

    }

    @Test
    public void testRefreshToken() {

        RequestRefreshToken requestRefreshToken = new RequestRefreshToken();
        requestRefreshToken.setRefreshToken("token");

        Assertions.assertThrows(HttpException.class, () -> userService.refreshToken(requestRefreshToken));

        Mockito.verify(refreshTokenService, Mockito.times(1)).findActiveToken(eq("token"));

    }

    @Test
    public void testUpdateUserProfile() {

        URequestUserProfile requestUserProfile = new URequestUserProfile();
        requestUserProfile.setName("Kiet");
        requestUserProfile.setPhone("0963987949");
        requestUserProfile.setDob("09/10/2002");

        userService.updateUserProfile(requestUserProfile);

        Mockito.verify(userRepository, Mockito.times(1)).findById(any(Integer.class));
        Mockito.verify(dateFormatter, Mockito.times(1)).formatDate(any(String.class));
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));

    }

    @Test
    public void testUpdateUserRole() {

        userService.updateUserRole(0, "admin");

        Mockito.verify(userRepository, Mockito.times(1)).findById(any(Integer.class));
        Mockito.verify(userPermissionRepository, Mockito.times(1)).findByName(eq("admin"));
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUserPassword() {

        URequestUserPassword requestUserPassword = new URequestUserPassword("123456", "1234567");

        Assertions.assertThrows(HttpException.class, () -> userService.updateUserPassword(requestUserPassword));
        Mockito.verify(bCryptPasswordEncoder, Mockito.times(1)).matches(any(String.class), any(String.class));
    }

    @Test
    public void testUpdateUserAvatar() {

        userService.updateUserAvatar(null);

        Mockito.verify(resourceService, Mockito.times(1)).addFile(any());
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void testDisableUser() {

        userService.disableUser(0);

        Mockito.verify(userRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void testCreateFriendRequest() {

        Assertions.assertThrows(HttpException.class, () -> userService.createFriendRequest(0));

        Mockito.verify(userRepository, Mockito.times(2)).findById(eq(0));
        Mockito.verify(userFriendshipRepository, Mockito.times(1)).findByUser(any(User.class), any(User.class));
    }

    @Test
    public void testAcceptFriendRequest() {

        Assertions.assertThrows(HttpException.class, () -> userService.acceptFriendRequest(0));

    }

    @Test
    public void testRejectFriendRequest() {

        Assertions.assertThrows(HttpException.class, () -> userService.rejectFriendRequest(0));

    }

    @Test
    public void testUnFriend() {

        userService.unFriend(0);

        Mockito.verify(userFriendshipRepository, Mockito.times(1)).save(any(UserFriendship.class));
    }

    @Test
    public void testFindFriendship() {

        Assertions.assertThrows(HttpException.class, () -> userService.findFriendship(11));

        userService.findFriendship(0);

        Mockito.verify(userRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(userFriendshipRepository, Mockito.times(1)).findByUser(any(User.class), any(User.class));
    }

}
