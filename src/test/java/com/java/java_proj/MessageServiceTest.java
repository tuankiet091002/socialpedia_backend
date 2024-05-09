package com.java.java_proj;

import com.java.java_proj.repositories.ChannelMemberRepository;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.services.MessageServiceImpl;
import com.java.java_proj.services.templates.ChannelService;
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
public class MessageServiceTest {

    @InjectMocks
    private MessageServiceImpl messageService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ChannelMemberRepository channelMemberRepository;
    @Mock
    private InboxRepository inboxRepository;
    @Mock
    private ResourceService resourceService;
    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ChannelService channelService;
    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    public void setMessageRepository() {

    }

    @BeforeEach
    public void setChannelRepository() {

    }

    @BeforeEach
    public void setChannelMemberRepository() {

    }

    @BeforeEach
    public void setInboxRepository() {

    }

    @BeforeEach
    public void setResourceService() {

    }

    @BeforeEach
    public void setUserService() {

    }

    @BeforeEach
    public void setNotificationService() {

    }

    @BeforeEach
    public void setChannelService() {

    }
}
