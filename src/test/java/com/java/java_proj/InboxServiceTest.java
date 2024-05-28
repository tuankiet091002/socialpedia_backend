package com.java.java_proj;

import com.java.java_proj.dto.request.forupdate.URequestInbox;
import com.java.java_proj.dto.response.fordetail.DResponseInbox;
import com.java.java_proj.dto.response.forlist.LResponseInbox;
import com.java.java_proj.entities.Inbox;
import com.java.java_proj.entities.Message;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserFriendship;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
@MockitoSettings(strictness = Strictness.LENIENT)
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
            Inbox inbox = Inbox.builder()
                    .id(i)
                    .isActive(true)
                    .friendship(UserFriendship.builder()
                            .sender(User.builder().id(0).build())
                            .receiver(User.builder().id(1).build()).build())
                    .senderLastSeen(Message.builder().id(0).build())
                    .receiverLastSeen(Message.builder().id(1).build())
                    .build();

            inboxes.add(inbox);
        }

        Mockito.when(inboxRepository.findByNameAndUser(any(String.class), any(User.class), any(Pageable.class))).thenReturn(new PageImpl<>(inboxes, pageable, inboxes.size()));
        Mockito.when(inboxRepository.findById(any(Integer.class))).thenReturn(Optional.of(inboxes.get(0)));
        Mockito.when(inboxRepository.save(any(Inbox.class))).thenReturn(null);
        Mockito.when(inboxRepository.findByFriendshipAndIsActive(any(UserFriendship.class), eq(true))).thenReturn(Optional.ofNullable(inboxes.get(0)));
    }

    @BeforeEach
    public void setMessageRepository() {
        Message message = Message.builder().id(0).build();

        Mockito.when(messageRepository.findByInbox(any(Inbox.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(message), PageRequest.of(0, 10), 1));
    }

    @BeforeEach
    public void setUserService() {
        Mockito.when(userService.getOwner()).thenReturn(User.builder().id(0).build());
        Mockito.when(userService.findFriendship(any(Integer.class))).thenReturn(UserFriendship.builder().sender(User.builder().id(0).build()).receiver(User.builder().id(1).build()).build());
    }

    @BeforeEach
    public void setModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

    ///////////////////////////////////////////////////////////////////
    ////////////////////////// TESTING PHASE //////////////////////////
    ///////////////////////////////////////////////////////////////////

    @Test
    public void testGetInboxList() {
        Page<LResponseInbox> inboxPage = inboxService.getInboxList("", 0, 10);

        Assertions.assertEquals(10, inboxPage.getContent().size());
        Mockito.verify(userService, Mockito.times(1)).getOwner();
        Mockito.verify(inboxRepository, Mockito.times(1)).findByNameAndUser(any(String.class), any(User.class), any(Pageable.class));
        Mockito.verify(modelMapper, Mockito.times(10)).map(any(Inbox.class), any());
        Mockito.verify(messageRepository, Mockito.times(10)).findByInbox(any(Inbox.class), any(Pageable.class));
    }

    @Test
    public void testGetInboxProfile() {

        DResponseInbox channel = inboxService.getInboxProfile(0);

        Assertions.assertEquals(0, channel.getId());
        Mockito.verify(inboxRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(modelMapper, Mockito.times(1)).map(any(Inbox.class), any());
    }

    @Test
    public void testCreateInbox() {

        inboxService.createInbox(0);

        Mockito.verify(inboxRepository, Mockito.times(1)).countByFriendshipAndIsActive(any(UserFriendship.class), eq(true));
        Mockito.verify(inboxRepository, Mockito.times(1)).save(any(Inbox.class));
    }

    @Test
    public void testUpdateInboxProfile() {

        URequestInbox requestInbox = new URequestInbox("Inbox");

        inboxService.updateInboxProfile(0, requestInbox);

        Mockito.verify(inboxRepository, Mockito.times(1)).findByFriendshipAndIsActive(any(UserFriendship.class), eq(true));
        Mockito.verify(inboxRepository, Mockito.times(1)).save(any(Inbox.class));
    }
}
