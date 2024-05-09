package com.java.java_proj;

import com.java.java_proj.repositories.ChannelMemberRepository;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.ChannelServiceImpl;
import com.java.java_proj.services.templates.NotificationService;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

    @InjectMocks
    private ChannelServiceImpl channelService;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ChannelMemberRepository channelMemberRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ResourceService resourceService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserService userService;
    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    public void setChannelRepository() {

    }

    @BeforeEach
    public void setChannelMemberRepository() {

    }

    @BeforeEach
    public void setUserRepository() {

    }

    @BeforeEach
    public void setMessageRepository() {

    }

    @BeforeEach
    public void setResourceService() {

    }

    @BeforeEach
    public void setNotificationService() {

    }

    @BeforeEach
    public void setUserService() {

    }

}
