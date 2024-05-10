package com.java.java_proj;

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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    }

    @BeforeEach
    public void setUserPermissionRepository() {

    }

    @BeforeEach
    public void setUserFriendshipRepository() {

    }

    @BeforeEach
    public void setInboxRepository() {

    }

    @BeforeEach
    public void setResourceService() {

    }

    @BeforeEach
    public void setRefreshTokenService() {

    }

    @BeforeEach
    public void setNotificationService() {

    }
    
}
