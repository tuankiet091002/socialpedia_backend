package com.java.java_proj;

import com.java.java_proj.dto.request.forcreate.CRequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannelMember;
import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.dto.response.forlist.LResponseChannel;
import com.java.java_proj.entities.*;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ChannelMemberRepository;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.ChannelServiceImpl;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
@MockitoSettings(strictness = Strictness.LENIENT)
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
        Pageable pageable = PageRequest.of(0, 10);
        List<Channel> channels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Channel channel = new Channel();
            channel.setId(i);
            channel.setName("Channel " + i);
            channel.setDescription("Description " + i);
            channel.setIsActive(true);

            channels.add(channel);
        }

        Mockito.when(channelRepository.findByNameContaining(any(String.class), any(Pageable.class))).thenReturn(new PageImpl<>(channels, pageable, channels.size()));
        Mockito.when(channelRepository.findPersonalChannelList(any(String.class), any(User.class), any(Pageable.class))).thenReturn(new PageImpl<>(channels, pageable, channels.size()));
        Mockito.when(channelRepository.findById(any(Integer.class))).thenReturn(Optional.of(channels.get(0)));
        Mockito.when(channelRepository.findOneById(any(Integer.class))).thenReturn(Optional.of(channels.get(0)));
        Mockito.when(channelRepository.save(any(Channel.class))).thenReturn(null);
    }

    @BeforeEach
    public void setChannelMemberRepository() {
        ChannelMember channelMember = ChannelMember.builder().member(User.builder().id(0).build()).channel(Channel.builder().id(0).build()).status(RequestType.ACCEPTED).channelPermission(PermissionAccessType.CREATE).memberPermission(PermissionAccessType.CREATE).messagePermission(PermissionAccessType.CREATE).build();

        Mockito.when(channelMemberRepository.findByChannelAndMember(any(Channel.class), any(User.class))).thenReturn(Optional.of(channelMember));
        Mockito.when(channelMemberRepository.save(any(ChannelMember.class))).thenReturn(null);

    }

    @BeforeEach
    public void setUserRepository() {
        User user = User.builder().id(0).build();

        Mockito.when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
    }

    @BeforeEach
    public void setMessageRepository() {
        Message message = Message.builder().id(0).build();

        Mockito.when(messageRepository.findByChannel(any(Channel.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(message), PageRequest.of(0, 10), 1));
    }

    @BeforeEach
    public void setResourceService() {

        Mockito.when(resourceService.addFile(any(MultipartFile.class)))
                .thenReturn(Resource.builder().id(0).filename("filename").fileSize(100000L).fileType("pdf").url("https://drive.google.com").generatedName("iajhrcnalrhaneioubciwreuoawiernmawrcawopdf").build());

        Mockito.doNothing().when(resourceService).deleteFile(any(Integer.class));
    }

    @BeforeEach
    public void setNotificationService() {
        Mockito.doNothing().when(notificationService).channelRequestSend(any(User.class), any(Channel.class));

        Mockito.doNothing().when(notificationService).channelRequestAccepted(any(Channel.class), any(User.class));

        Mockito.doNothing().when(notificationService).seenByDestination(any(String.class));
    }

    @BeforeEach
    public void setUserService() {
        Mockito.when(userService.getOwner()).thenReturn(User.builder().id(0).build());
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
    public void testGetChannelList() {

        Page<LResponseChannel> channelPage = channelService.getChannelList("", 0, 10, "id", "DESC");

        Assertions.assertEquals(10, channelPage.getContent().size());
        Mockito.verify(channelRepository, Mockito.times(1)).findByNameContaining(any(String.class), any(Pageable.class));
        Mockito.verify(modelMapper, Mockito.times(10)).map(any(Channel.class), any());
        Mockito.verify(channelMemberRepository, Mockito.times(10)).countByChannel(any(Channel.class));
    }

    @Test
    public void testGetPersonalChannelList() {

        Page<LResponseChannel> channelPage = channelService.getPersonalChannelList("", 0, 10);

        Assertions.assertEquals(10, channelPage.getContent().size());
        Mockito.verify(channelRepository, Mockito.times(1)).findPersonalChannelList(any(String.class), any(User.class), any(Pageable.class));
        Mockito.verify(modelMapper, Mockito.times(10)).map(any(Channel.class), any());
        Mockito.verify(messageRepository, Mockito.times(10)).findByChannel(any(Channel.class), any(Pageable.class));
    }

    @Test
    public void testGetChannelProfile() {

        DResponseChannel channel = channelService.getChannelProfile(0);

        Assertions.assertEquals(0, channel.getId());
        Mockito.verify(channelRepository, Mockito.times(1)).findOneById(eq(0));
        Mockito.verify(modelMapper, Mockito.times(1)).map(any(Channel.class), any());
    }

    @Test
    public void testGetChannelRelation() {

        channelService.getChannelRelation(0);

        Mockito.verify(channelRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(channelMemberRepository, Mockito.times(1)).findByChannelAndMember(any(Channel.class), any(User.class));
        Mockito.verify(modelMapper, Mockito.times(1)).map(any(ChannelMember.class), any());
    }

    @Test
    public void testCreateChannel() {

        CRequestChannel channel = new CRequestChannel("Channel", "Description", null, new ArrayList<>());
        channelService.createChannel(channel);

        Mockito.verify(channelRepository, Mockito.times(1)).save(any(Channel.class));
    }

    @Test
    public void testUpdateChannelProfile() {

        URequestChannel requestChannel = new URequestChannel("Channel", "Description");

        channelService.updateChannelProfile(0, requestChannel);

        Mockito.verify(channelRepository, Mockito.times(1)).findById(any(Integer.class));
        Mockito.verify(channelRepository, Mockito.times(1)).save(any(Channel.class));
    }

    @Test
    public void testUpdateChannelAvatar() {

        channelService.updateChannelAvatar(0, null);

        Mockito.verify(channelRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(resourceService, Mockito.times(1)).addFile(any());
    }

    @Test
    public void testDisableChannel() {

        channelService.disableChannel(0);

        Mockito.verify(channelRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(channelRepository, Mockito.times(1)).save(any(Channel.class));
    }

    @Test
    public void testCreateChannelRequest() {

        Assertions.assertThrows(HttpException.class, () -> channelService.createChannelRequest(0));

        Mockito.verify(channelRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(channelMemberRepository, Mockito.times(1)).findByChannelAndMember(any(Channel.class), any(User.class));
    }

    @Test
    public void testAcceptChannelRequest() {

        Assertions.assertThrows(HttpException.class, () -> channelService.acceptChannelRequest(0, 0));
    }

    @Test
    public void testRejectChannelRequest() {

        Assertions.assertThrows(HttpException.class, () -> channelService.rejectChannelRequest(0, 0));
    }

    @Test
    public void testUpdateMemberPermission() {
        URequestChannelMember requestChannelMember = new URequestChannelMember(PermissionAccessType.CREATE, PermissionAccessType.CREATE, PermissionAccessType.CREATE);

        channelService.updateMemberPermission(0, 0, requestChannelMember);

        Mockito.verify(channelMemberRepository, Mockito.times(1)).save(any(ChannelMember.class));
    }

    @Test
    public void testLeaveChannel() {

        channelService.leaveChannel(0);

        Mockito.verify(channelMemberRepository, Mockito.times(1)).save(any(ChannelMember.class));
    }

    @Test
    public void testFindMemberRequest() {

        channelService.findMemberRequest(0, 0);

        Mockito.verify(channelRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(userRepository, Mockito.times(1)).findById(eq(0));
        Mockito.verify(channelMemberRepository, Mockito.times(1)).findByChannelAndMember(any(Channel.class), any(User.class));
    }

}
