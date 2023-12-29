package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannelMember;
import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.dto.response.forlist.LResponseChatSpace;
import com.java.java_proj.dto.response.forlist.LResponseMessage;
import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;
import com.java.java_proj.entities.*;
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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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

            // convert missing fields
            ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
            channel.setAvatar(pf.createProjection(DResponseResource.class, entity.getAvatar()));
            channel.setCreatedBy(pf.createProjection(LResponseUserMinimal.class, entity.getCreatedBy()));


            // fetch top message and skip the pageable part
            List<Message> messageList = messageRepository.findByChannel("", entity, PageRequest.of(0, 1))
                    .getContent();
            if (!messageList.isEmpty()) {
                channel.setLatestMessage(pf.createProjection(LResponseMessage.class, messageList.get(0)));
            }

            return channel;
        });
    }

    @Override
    public Page<LResponseChatSpace> getPersonalChannelList(String name, Integer page, Integer size) {
        // fetch user
        User user = userService.getOwner();

        // create pageable
        Pageable paging = PageRequest.of(page, size);

        // fetch entity page
        Page<Channel> channelPage = channelRepository.findPersonalChannelList(name, user, paging);

        // map to dto
        return channelPage.map(entity -> {

            // map to dto
            LResponseChatSpace channel = modelMapper.map(entity, LResponseChatSpace.class);

            // convert missing fields
            ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
            channel.setAvatar(pf.createProjection(DResponseResource.class, entity.getAvatar()));
            channel.setCreatedBy(pf.createProjection(LResponseUserMinimal.class, entity.getCreatedBy()));

            // fetch top message and skip the pageable part
            List<Message> messageList = messageRepository.findByChannel("", entity, PageRequest.of(0, 1))
                    .getContent();
            if (!messageList.isEmpty()) {
                channel.setLatestMessage(pf.createProjection(LResponseMessage.class, messageList.get(0)));
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
    @Transactional
    public void createChannel(CRequestChannel requestChannel) {

        // model map
        Channel channel = modelMapper.map(requestChannel, Channel.class);

        // set normal field
        channel.setCreatedBy(userService.getOwner());
        channel.setCreatedDate(LocalDateTime.now());
        channel.setIsActive(true);

        // avatar file check
        if (requestChannel.getAvatarFile() != null) {
            Resource resource = resourceService.addFile(requestChannel.getAvatarFile());
            channel.setAvatar(resource);
        }

        // save entity
        channel = channelRepository.save(channel);

        // save middle entities
        // intellij's recommended
        Channel finalChannel = channel;
        requestChannel.getChannelMembersId().forEach(request -> {

            // corresponding user
            User user = userRepository.findById(request.getMemberId())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel member with id " + request.getMemberId() + " is not found"));

            channelMemberRepository.save(ChannelMember.builder()
                    .member(user)
                    .channel(finalChannel)
                    .status(RequestType.ACCEPTED)
                    .memberPermission(PermissionAccessType.VIEW)
                    .messagePermission(PermissionAccessType.VIEW)
                    .lastSeenMessage(null)
                    .build());
        });
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

        // mark old avatar to delete
        Integer oldResource = null;
        if (channel.getAvatar() != null) {
            oldResource = channel.getAvatar().getId();
        }

        // set and save
        Resource resource = resourceService.addFile(file);
        channel.setAvatar(resource);
        channel.setModifiedBy(userService.getOwner());
        channel.setModifiedDate(LocalDateTime.now());

        channelRepository.save(channel);

        if (oldResource != null)
            resourceService.deleteFile(oldResource);
    }

    @Override
    public void disableChannel(Integer channelId) {

        // check channel
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found"));

        // set fields
        channel.setIsActive(false);
        channel.setModifiedBy(userService.getOwner());
        channel.setModifiedDate(LocalDateTime.now());

        channelRepository.save(channel);
    }

    @Override
    @Transactional
    public void createChannelRequest(Integer channelId) {

        // fetch channel from id
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));

        // fetch user from id
        User member = userRepository.findById(userService.getOwner().getId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found."));

        if (channelMemberRepository.findByChannelAndMember(channel, member).isPresent()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "You are already member of this channel.");
        }

        ChannelMember channelMember = (ChannelMember.builder()
                .channel(channel)
                .member(member)
                .status(RequestType.PENDING)
                .memberPermission(PermissionAccessType.VIEW)
                .messagePermission(PermissionAccessType.VIEW)
                .build());

        channelMemberRepository.save(channelMember);
    }

    @Override
    @Transactional
    public void acceptChannelRequest(Integer channelId, Integer memberId) {

        ChannelMember channelMember = findMemberRequest(channelId, memberId);

        // change request status
        if (channelMember.getStatus() != RequestType.PENDING)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Member request is not pending");
        channelMember.setStatus(RequestType.ACCEPTED);

        channelMemberRepository.save(channelMember);
    }

    @Override
    @Transactional
    public void rejectChannelRequest(Integer channelId, Integer memberId) {

        ChannelMember channelMember = findMemberRequest(channelId, memberId);

        // change request status
        if (channelMember.getStatus() != RequestType.PENDING)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Member request is not pending");
        channelMember.setStatus(RequestType.REJECTED);

        channelMemberRepository.save(channelMember);

    }

    @Override
    @Transactional
    public void updateMemberPermission(Integer channelId, Integer memberId, URequestChannelMember requestChannel) {

        ChannelMember channelMember = findMemberRequest(channelId, memberId);

        // change permission
        if (channelMember.getStatus() != RequestType.ACCEPTED)
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is not a member.");
        channelMember.setMessagePermission(requestChannel.getMessagePermission());
        channelMember.setMemberPermission(requestChannel.getMemberPermission());

        channelMemberRepository.save(channelMember);
    }

    @Override
    @Transactional
    public void unMember(Integer channelId, Integer memberId) {

        ChannelMember channelMember = findMemberRequest(channelId, memberId);

        // change request status
        if (channelMember.getStatus() != RequestType.ACCEPTED)
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is not a member.");
        channelMember.setStatus(RequestType.REJECTED);

        channelMemberRepository.save(channelMember);
    }

    private ChannelMember findMemberRequest(Integer channelId, Integer userId) {

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
