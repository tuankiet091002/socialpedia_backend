package com.java.java_proj;

import com.java.java_proj.dto.request.forupdate.URequestInbox;
import com.java.java_proj.dto.response.fordetail.DResponseInbox;
import com.java.java_proj.dto.response.forlist.LResponseChatSpace;
import com.java.java_proj.entities.*;
import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.services.InboxServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
        Pageable pageable = PageRequest.of(0, 10);
        List<Inbox> inboxes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Inbox inbox = new Inbox();
            inbox.setId(i);
            inbox.setName("Channel " + i);
            inbox.setIsActive(true);

            inboxes.add(inbox);
        }

        Mockito.when(inboxRepository.findByNameAndUser(any(String.class), any(User.class), any(Pageable.class))).thenReturn(new PageImpl<>(inboxes, pageable, inboxes.size()));
        Mockito.doNothing().when(inboxRepository).save(any(Inbox.class));
        Mockito.when(inboxRepository.findByFriendshipAndIsActive(any(UserFriendship.class), eq(true))).thenReturn(Optional.ofNullable(inboxes.get(0)));
    }

    @BeforeEach
    public void setMessageRepository() {
        Message message = Message.builder().id(0).build();

        Mockito.when(messageRepository.findByInbox("", any(Inbox.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(message), PageRequest.of(0, 10), 1));
    }

    @BeforeEach
    public void setUserService() {
        Mockito.when(userService.getOwner()).thenReturn(User.builder().id(0).build());
    }

    ///////////////////////////////////////////////////////////////////
    ////////////////////////// TESTING PHASE //////////////////////////
    ///////////////////////////////////////////////////////////////////

    @Test
    public void testGetInboxList() {
        Page<LResponseChatSpace> inboxPage = inboxService.getInboxList("", 0, 10);

        Assertions.assertEquals(10, inboxPage.getContent().size());
        Mockito.verify(userService, Mockito.times(1)).getOwner();
        Mockito.verify(inboxRepository, Mockito.times(1)).findByNameAndUser(any(String.class), any(User.class), any(Pageable.class));
        Mockito.verify(modelMapper, Mockito.times(10)).map(any(Channel.class), any());
        Mockito.verify(messageRepository, Mockito.times(10)).findByInbox(any(String.class), any(Inbox.class), any(Pageable.class));
    }

    @Test
    public void testGetInboxProfile() {

        DResponseInbox channel = inboxService.getInboxProfile(0);

        Assertions.assertEquals(0, channel.getId());
        Mockito.verify(inboxRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(modelMapper, Mockito.times(10)).map(any(Channel.class), any());
    }

    @Test
    public void testCreateInbox() {

        inboxService.createInbox(0);

        Mockito.verify(inboxRepository, Mockito.times(1)).countByFriendshipAndIsActive(any(UserFriendship.class), eq(true));
        Mockito.verify(inboxRepository, Mockito.times(1)).save(any(Inbox.class));
    }

    @Test
    public void testUpdateInboxProfile() {

        URequestInbox requestInbox = new URequestInbox("Inbox", true);

        inboxService.updateInboxProfile(0, requestInbox);

        Mockito.verify(inboxRepository, Mockito.times(1)).findByFriendshipAndIsActive(any(UserFriendship.class), eq(true));
        Mockito.verify(inboxRepository, Mockito.times(1)).save(any(Inbox.class));
    }
}
