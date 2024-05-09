package com.java.java_proj;

import com.java.java_proj.repositories.NotificationRepository;
import com.java.java_proj.services.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationServiceImpl notificationService;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    public void setNotificationRepository() {

    }

    @BeforeEach
    public void setMessagingTemplate() {

    }
}
