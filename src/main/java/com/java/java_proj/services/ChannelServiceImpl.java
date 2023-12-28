package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestChannel;
import com.java.java_proj.dto.request.forcreate.CRequestChannelMember;
import com.java.java_proj.dto.request.forupdate.URequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannelMember;
import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.dto.response.forlist.LResponseChatSpace;
import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.ChannelMember;
import com.java.java_proj.entities.Resource;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ChannelMemberRepository;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.ChannelService;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelServiceImpl implements ChannelService {

    final private ChannelRepository channelRepository;
    final private ChannelMemberRepository channelMemberRepository;
    final private UserRepository userRepository;
    final private MessageRepository messageRepository;
    final private ResourceService resourceService;
    final private UserService userService;
    final private ModelMapper modelMapper;

    public ChannelServiceImpl(ChannelRepository channelRepository, ChannelMemberRepository channelMemberRepository, UserRepository userRepository, MessageRepository messageRepository, ResourceService resourceService, UserService userService, ModelMapper modelMapper) {
        this.channelRepository = channelRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.resourceService = resourceService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<LResponseChatSpace> getChannelList(String name, Integer page, Integer size, String orderBy, String orderDirection) {

        // create pageable
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(page, size, Sort.by(orderBy).ascending())
                : PageRequest.of(page, size, Sort.by(orderBy).descending());

        // fetch entity page
        Page<Channel> channelPage = channelRepository.findByNameContaining(name, paging);

        // map to dto
        return channelPage.map(entity -> {
            // map to dto
            LResponseChatSpace channel = modelMapper.map(entity, LResponseChatSpace.class);

            // fetch top message and skip the pageable part
            List<DResponseMessage> messageList = messageRepository.findByChannel("", entity, PageRequest.of(0, 1))
                    .getContent();
            if (!messageList.isEmpty()) {
                channel.setLatestMessage(messageList.get(0));
            }

            return channel;
        });
    }

    @Override
    public Page<LResponseChatSpace> getPersonalChannelList(String name, Integer page, Integer size, String orderBy, String orderDirection) {
        // fetch user
        User user = userService.getOwner();

        // create pageable
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(page, size, Sort.by(orderBy).ascending())
                : PageRequest.of(page, size, Sort.by(orderBy).descending());

        // fetch entity page
        Page<Channel> channelPage = channelRepository.findPersonalChannelList(name, user, paging);

        // map to dto
        return channelPage.map(entity -> {
            // map to dto
            LResponseChatSpace channel = modelMapper.map(entity, LResponseChatSpace.class);

            // fetch top message and skip the pageable part
            List<DResponseMessage> messageList = messageRepository.findByChannel("", entity, PageRequest.of(0, 1))
                    .getContent();
            if (!messageList.isEmpty()) {
                channel.setLatestMessage(messageList.get(0));
            }

            return channel;
        });
    }

    @Override
    public DResponseChannel getChannelProfile(Integer id) {

        return channelRepository.findOneById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));
    }

    @Override
    public void createChannel(CRequestChannel requestChannel) {

        // model map
        Channel channel = modelMapper.map(requestChannel, Channel.class);

        // avatar file check
        if (requestChannel.getAvatarFile() != null) {
            Resource resource = resourceService.addFile(requestChannel.getAvatarFile());
            channel.setAvatar(resource);
        }

        // set creator
        channel.setCreatedBy(userService.getOwner());
        channel.setCreatedDate(LocalDateTime.now());

        channel = channelRepository.save(channel);

        // set member list after save once
        channel.setChannelMembers(genChannelMemberList(channel.getId(),
                requestChannel.getChannelMembersId()));

        channelRepository.save(channel);
    }

    @Override
    public void updateChannelProfile(Integer channelId, URequestChannel requestChannel) {

        // check channel
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found"));

        // set normal field
        channel.setName(requestChannel.getName());
        channel.setDescription(requestChannel.getDescription());

        // set modified user
        channel.setModifiedBy(userService.getOwner());
        channel.setModifiedDate(LocalDateTime.now());

        channelRepository.save(channel);
    }

    @Override
    public void updateChannelAvatar(Integer channelId, MultipartFile file) {

        // check channel
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found"));

        if (channel.getAvatar() != null) {
            resourceService.deleteFile(channel.getAvatar().getId());
        }

        Resource resource = resourceService.addFile(file);
        channel.setAvatar(resource);

        channelRepository.save(channel);
    }

    @Override
    public void disableChannel(Integer channelId) {

        // check channel
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found"));

        // check active status
        if (!channel.getIsActive()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is already inactive");
        }
        channel.setIsActive(false);

        channelRepository.save(channel);
    }

    @Override
    public void createChannelRequest(Integer channelId) {

        // fetch channel from id
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));

        // fetch user from id
        User member = userService.getOwner();

        ChannelMember channelMember = channelMemberRepository.findByChannelAndMember(channel, member)
                .orElse(ChannelMember.builder()
                        .channel(channel)
                        .member(member)
                        .status(RequestType.PENDING)
                        .memberPermission(PermissionAccessType.VIEW)
                        .messagePermission(PermissionAccessType.VIEW)
                        .build());

        channelMemberRepository.save(channelMember);
    }

    @Override
    public void acceptChannelRequest(Integer channelId, Integer memberId) {

        ChannelMember channelMember = findMember(channelId, memberId);

        // change request status
        if (channelMember.getStatus() != RequestType.PENDING)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Member request is not pending");
        channelMember.setStatus(RequestType.ACCEPTED);

        channelMemberRepository.save(channelMember);
    }

    @Override
    public void rejectChannelRequest(Integer channelId, Integer memberId) {

        ChannelMember channelMember = findMember(channelId, memberId);
        // change request status
        if (channelMember.getStatus() != RequestType.PENDING)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Member request is not pending");
        channelMember.setStatus(RequestType.REJECTED);

        channelMemberRepository.save(channelMember);

    }

    @Override
    public void updateMemberPermission(Integer channelId, Integer memberId, URequestChannelMember requestChannel) {

    }

    @Override
    public void unMember(Integer channelId, Integer memberId) {

        ChannelMember channelMember = findMember(channelId, memberId);
        // change request status
        if (channelMember.getStatus() != RequestType.ACCEPTED)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Member request is not pending");
        channelMember.setStatus(RequestType.REJECTED);

        channelMemberRepository.save(channelMember);
    }

    private List<ChannelMember> genChannelMemberList(Integer channelId, List<CRequestChannelMember> requestChannelList) {

        return requestChannelList.stream()
                .map(request -> {

                    // dummy channel
                    Channel dummy = new Channel();
                    dummy.setId(channelId);

                    // corresponding user
                    User user = userRepository.findById(request.getMemberId())
                            .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel member with id " + request.getMemberId() + " is not found"));

                    return ChannelMember.builder()
                            .member(user)
                            .channel(dummy)
                            .status(RequestType.ACCEPTED)
                            .memberPermission(PermissionAccessType.VIEW)
                            .lastSeenMessage(null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private ChannelMember findMember(Integer channelId, Integer userId) {

        // fetch channel from id
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));

        // fetch user from id
        User member = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found."));

        return channelMemberRepository.findByChannelAndMember(channel, member)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Membership not found."));
    }
}
