package com.java.java_proj;

import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.Notification;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.miscs.SocketMessage;
import com.java.java_proj.repositories.NotificationRepository;
import com.java.java_proj.services.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.ArgumentMatchers.any;

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

        Mockito.doNothing().when(notificationRepository).save(any());
        Mockito.doNothing().when(notificationRepository).seenByDestination(any(String.class));
        Mockito.doNothing().when(notificationRepository).seenByUserAndDestination(any(User.class), any(String.class));
    }

    @BeforeEach
    public void setMessagingTemplate() {
        Mockito.doNothing().when(messagingTemplate).convertAndSend(any(String.class), any(SocketMessage.class));
    }

    //////////////////////////////////////////////////////////////////
    ////////////////////////// TESTING PHASE //////////////////////////
    ///////////////////////////////////////////////////////////////////

    @Test
    public void testGetNotificationList() {

        notificationService.getNotificationList(0, 10);

        Mockito.verify(notificationRepository, Mockito.times(1)).findByUserOrderByIdDesc(any(User.class), any());
    }

    @Test
    public void testFriendRequestSend() {

        notificationService.friendRequestSend(User.builder().id(0).build(), User.builder().id(1).build());

        Mockito.verify(notificationRepository, Mockito.times(1)).save(any(Notification.class));
        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(any(String.class), any(SocketMessage.class));

    }

    @Test
    public void testFriendRequestAccepted() {

        notificationService.friendRequestAccepted(User.builder().id(0).build(), User.builder().id(1).build());

        Mockito.verify(notificationRepository, Mockito.times(1)).save(any(Notification.class));
        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(any(String.class), any(SocketMessage.class));
    }

    @Test
    public void testChannelRequestSend() {
        notificationService.channelRequestSend(User.builder().id(0).build(), Channel.builder().id(1).build());

        Mockito.verify(notificationRepository, Mockito.times(1)).save(any(Notification.class));
        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(any(String.class), any(SocketMessage.class));
    }

    @Test
    public void testChannelRequestAccepted() {

        notificationService.channelRequestAccepted(Channel.builder().id(1).build(), User.builder().id(0).build());

        Mockito.verify(notificationRepository, Mockito.times(1)).save(any(Notification.class));
        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(any(String.class), any(SocketMessage.class));
    }

    @Test
    public void testMessageToChannel() {

        notificationService.messageToChannel(0);

        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(any(String.class), any(SocketMessage.class));
    }

    @Test
    public void testMessageToInbox() {

        notificationService.messageToInbox(0);

        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(any(String.class), any(SocketMessage.class));
    }

    @Test
    public void testSeenByDestination() {
        notificationService.seenByDestination("");

        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(any(String.class), any(SocketMessage.class));
    }

    @Test
    public void testSeenByUserAndDestination() {

        notificationService.seenByUserAndDestination(User.builder().id(0).build(), "");

        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(any(String.class), any(SocketMessage.class));
    }

}
