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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NotificationServiceTest {

    @Spy
    @InjectMocks
    private NotificationServiceImpl notificationService;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    public void setNotificationService() {
        Mockito.doReturn(User.builder().id(0).password("123456").build()).when(notificationService).getOwner();
    }

    @BeforeEach
    public void setNotificationRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Notification notification = new Notification();
            notification.setId(i);
            notification.setTitle("Title " + i);
            notification.setContent("Content " + i);

            notifications.add(notification);
        }

        Mockito.when(notificationRepository.findByUserOrderByIdDesc(any(User.class), any(Pageable.class))).thenReturn(new PageImpl<>(notifications, pageable, notifications.size()));
        Mockito.when(notificationRepository.save(any())).thenReturn(null);
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

        Mockito.verify(notificationRepository, Mockito.times(1)).findByUserOrderByIdDesc(any(User.class), any(Pageable.class));
        Mockito.verify(modelMapper, Mockito.times(10)).map(any(Notification.class), any());
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

        Mockito.verify(notificationRepository, Mockito.times(1)).seenByDestination(any(String.class));
    }

    @Test
    public void testSeenByUserAndDestination() {

        notificationService.seenByUserAndDestination(User.builder().id(0).build(), "");

        Mockito.verify(notificationRepository, Mockito.times(1)).seenByUserAndDestination(any(User.class), any(String.class));
    }

}
