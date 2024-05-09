package com.java.java_proj;

import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.services.InboxServiceImpl;
import com.java.java_proj.services.templates.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class InboxServiceTest {

    @InjectMocks
    private InboxServiceImpl inboxService;
    @Mock
    private InboxRepository inboxRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserService userService;
    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    public void setInboxRepository() {

    }

    @BeforeEach
    public void setMessageRepository() {

    }

    @BeforeEach
    public void setUserService() {

    }

}
