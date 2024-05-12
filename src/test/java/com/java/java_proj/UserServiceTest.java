package com.java.java_proj;

import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.*;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.RequestType;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

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
    private AuthenticationManager authenticationManager;
    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Spy
    private ModelMapper modelMapper;
    @Spy
    private DateFormatter dateFormatter;

    @BeforeEach
    public void setUserRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setEmail("kiet" + i + 1 + "@gmail.com");

            users.add(user);
        }

        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        List<LResponseUser> responseUsers = users.stream().map(
                entity -> pf.createProjection(LResponseUser.class, entity)
        ).toList();

        Mockito.when(userRepository.findByNameContaining(any(String.class), any(Pageable.class))).thenReturn(new PageImpl<>(users, pageable, responseUsers.size()));

        Mockito.when(userRepository.findFriendsByName(any(String.class), any(User.class), any(Pageable.class))).thenReturn(new PageImpl<>(users, pageable, responseUsers.size()));

        Mockito.when(userRepository.findById(eq(0)))
                .thenReturn(Optional.ofNullable(users.get(0)));

        Mockito.when(userRepository.countByEmail(any(String.class)))
                .thenReturn(1);

        Mockito.when(userRepository.countByPhone(any(String.class)))
                .thenReturn(0);

        Mockito.when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(users.get(0)));

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

        Mockito.when(resourceService.addFile(any(MultipartFile.class)))
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

}
