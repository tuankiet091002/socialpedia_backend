package com.java.java_proj;

import com.java.java_proj.dto.request.forcreate.CRequestMessage;
import com.java.java_proj.dto.request.forupdate.URequestMessageProfile;
import com.java.java_proj.dto.request.forupdate.URequestMessageStatus;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.entities.*;
import com.java.java_proj.entities.enums.MessageStatusType;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.services.MessageServiceImpl;
import com.java.java_proj.services.templates.ChannelService;
import com.java.java_proj.services.templates.NotificationService;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageServiceImpl messageService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChannelRepository channelRepository;
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
        Pageable pageable = PageRequest.of(0, 10);
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setId(i);
            message.setContent("Message " + i);

            messages.add(message);
        }

        Mockito.when(messageRepository.findByChannel(null, any(Channel.class), any(Pageable.class))).thenReturn(new PageImpl<>(messages, pageable, messages.size()));
        Mockito.when(messageRepository.findByInbox(any(String.class), any(Inbox.class), any(Pageable.class))).thenReturn(new PageImpl<>(messages, pageable, messages.size()));
        Mockito.when(messageRepository.findByIdAndChannel(any(Integer.class), any(Channel.class))).thenReturn(Optional.of(messages.get(0)));
        Mockito.when(messageRepository.findByIdAndInbox(any(Integer.class), any(Inbox.class))).thenReturn(Optional.of(messages.get(0)));
        Mockito.when(messageRepository.countByLocation(any(Message.class), any(Integer.class))).thenReturn(1);
        Mockito.when(messageRepository.findById(any(Integer.class))).thenReturn(Optional.of(messages.get(0)));
    }


    @BeforeEach
    public void setChannelRepository() {

        Mockito.when(channelRepository.findById(any(Integer.class))).thenReturn(Optional.of(Channel.builder().id(0).build()));
    }

    @BeforeEach
    public void setInboxRepository() {

        Mockito.when(inboxRepository.findById(any(Integer.class))).thenReturn(Optional.of(Inbox.builder().id(0).build()));
    }

    @BeforeEach
    public void setResourceService() {

        Mockito.when(resourceService.addFile(any(MultipartFile.class)))
                .thenReturn(Resource.builder().id(0).filename("filename").fileSize(100000L).fileType("pdf").url("https://drive.google.com").generatedName("iajhrcnalrhaneioubciwreuoawiernmawrcawopdf").build());

        Mockito.doNothing().when(resourceService).deleteFile(any(Integer.class));
    }

    @BeforeEach
    public void setUserService() {
        Mockito.when(userService.getOwner()).thenReturn(User.builder().id(0).build());
    }

    @BeforeEach
    public void setNotificationService() {
        Mockito.doNothing().when(notificationService).messageToChannel(any(Integer.class));

        Mockito.doNothing().when(notificationService).messageToInbox(any(Integer.class));
    }

    ///////////////////////////////////////////////////////////////////
    ////////////////////////// TESTING PHASE //////////////////////////
    ///////////////////////////////////////////////////////////////////

    @Test
    public void testGetMessagesFromChannel() {

        Page<DResponseMessage> messages = messageService.getMessagesFromChannel(0, "", 0, 10);

        Assertions.assertEquals(10, messages.getContent().size());
        Mockito.verify(channelRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(messageRepository, Mockito.times(1)).findByChannel(null, any(Channel.class), PageRequest.of(0, 10));
    }

    @Test
    public void testGetMessagesFromInbox() {

        Page<DResponseMessage> messages = messageService.getMessagesFromInbox(0, "", 0, 10);

        Assertions.assertEquals(10, messages.getContent().size());
        Mockito.verify(inboxRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(messageRepository, Mockito.times(1)).findByInbox(null, any(Inbox.class), PageRequest.of(0, 10));
    }

    @Test
    public void testSendMessageToChannel() {

        CRequestMessage requestMessage = new CRequestMessage();
        requestMessage.setContent("Message");

        messageService.sendMessageToChannel(0, requestMessage);

        Mockito.verify(channelRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(userService, Mockito.times(1)).getOwner();
        Mockito.verify(channelRepository, Mockito.times(1)).save(any(Channel.class));
        Mockito.verify(notificationService, Mockito.times(1)).messageToChannel(eq(0));

    }

    @Test
    public void testSendMessageToInbox() {

        CRequestMessage requestMessage = new CRequestMessage();
        requestMessage.setContent("Message");

        messageService.sendMessageToChannel(0, requestMessage);

        Mockito.verify(inboxRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(userService, Mockito.times(1)).getOwner();
        Mockito.verify(inboxRepository, Mockito.times(1)).save(any(Inbox.class));
        Mockito.verify(notificationService, Mockito.times(1)).messageToInbox(eq(0));
    }

    @Test
    public void testUpdateMessageContent() {

        URequestMessageProfile requestMessage = new URequestMessageProfile();
        requestMessage.setId(0);
        requestMessage.setContent("Message");

        messageService.updateMessageContent(0, requestMessage);

        Mockito.verify(messageRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(messageRepository, Mockito.times(1)).countByLocation(any(Message.class), eq(0));
        Mockito.verify(userService, Mockito.times(1)).getOwner();
        Mockito.verify(messageRepository, Mockito.times(1)).findById(eq(0));
        ;
    }

    @Test
    public void testUpdateMessageStatus() {

        URequestMessageStatus requestMessage = new URequestMessageStatus();
        requestMessage.setId(0);
        requestMessage.setStatus(MessageStatusType.ACTIVE);

        messageService.updateMessageStatus(0, requestMessage);

        Mockito.verify(messageRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(messageRepository, Mockito.times(1)).countByLocation(any(Message.class), eq(0));
        Mockito.verify(messageRepository, Mockito.times(1)).findById(eq(0));
        ;
    }

    @Test
    public void testDeleteMessage() {

        messageService.deleteMessage(0, 0);

        Mockito.verify(messageRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(userService, Mockito.times(1)).getOwner();
        Mockito.verify(messageRepository, Mockito.times(1)).findById(eq(0));
        ;
    }

    @Test
    public void testSeeInboxMessage() {

        messageService.seeInboxMessage(0, 0, 1);

        Mockito.verify(messageRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(messageRepository, Mockito.times(1)).countByLocation(any(Message.class), eq(0));
        Mockito.verify(inboxRepository, Mockito.times(1)).save(any(Inbox.class));
    }

}
