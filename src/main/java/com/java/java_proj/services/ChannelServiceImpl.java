package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestChannel;
import com.java.java_proj.dto.request.forcreate.CRequestChannelMember;
import com.java.java_proj.dto.request.forupdate.URequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannelMember;
import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.dto.response.fordetail.DResponseChannelMember;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.forlist.LResponseChannel;
import com.java.java_proj.dto.response.forlist.LResponseMessage;
import com.java.java_proj.entities.*;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.entities.miscs.SocketMessage;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ChannelMemberRepository;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.ChannelService;
import com.java.java_proj.services.templates.NotificationService;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@CacheConfig(cacheNames = "channels")
public class ChannelServiceImpl implements ChannelService {

    final private ChannelRepository channelRepository;
    final private ChannelMemberRepository channelMemberRepository;
    final private UserRepository userRepository;
    final private MessageRepository messageRepository;
    final private ResourceService resourceService;
    final private NotificationService notificationService;
    final private RedisService redisService;
    final private UserService userService;
    final private ModelMapper modelMapper;
    final private SimpMessagingTemplate messagingTemplate;

    public ChannelServiceImpl(ChannelRepository channelRepository, ChannelMemberRepository channelMemberRepository, UserRepository userRepository, MessageRepository messageRepository, ResourceService resourceService, NotificationService notificationService, RedisService redisService, UserService userService, ModelMapper modelMapper, SimpMessagingTemplate messagingTemplate) {
        this.channelRepository = channelRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.resourceService = resourceService;
        this.notificationService = notificationService;
        this.redisService = redisService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Cacheable(key = "'page-' + {#pageNo, #pageSize, #orderBy, #orderDirection}", condition = "#name.isEmpty()")
    public Page<LResponseChannel> getChannelList(String name, Integer pageNo, Integer pageSize, String orderBy, String orderDirection) {

        // create pageable
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(pageNo, pageSize, Sort.by(
                Objects.equals(orderBy, "createdBy") ? "createdBy.name" : orderBy).ascending())
                : PageRequest.of(pageNo, pageSize, Sort.by(
                Objects.equals(orderBy, "createdBy") ? "createdBy.name" : orderBy).descending());

        // fetch entity page
        Page<Channel> channelPage = channelRepository.findByNameContaining(name, paging);

        // map to dto
        return channelPage.map(entity -> {
            // map to dto
            LResponseChannel channel = modelMapper.map(entity, LResponseChannel.class);

            // fetch member number
            channel.setMemberNum(channelMemberRepository.countByChannel(entity));

            return channel;
        });
    }

    @Override
    public Page<LResponseChannel> getPersonalChannelList(String name, Integer page, Integer size) {

        // fetch user
        User user = userService.getOwner();

        // create pageable
        Pageable paging = PageRequest.of(page, size);

        // fetch entity page
        Page<Channel> channelPage = channelRepository.findPersonalChannelList(name, user, paging);

        // map to dto
        return channelPage.map(entity -> {

            // map to dto
            LResponseChannel channel = modelMapper.map(entity, LResponseChannel.class);

            // fetch top message and skip the pageable part
            List<Message> messageList = messageRepository.findByChannel(entity,
                            PageRequest.of(0, 1, Sort.by("id").descending()))
                    .getContent();
            if (!messageList.isEmpty()) {
                channel.setLatestMessage(modelMapper.map(messageList.get(0), LResponseMessage.class));
            }

            return channel;
        });
    }

    @Override
    @Cacheable(key = "#id")
    @Transactional
    public DResponseChannel getChannelProfile(Integer id) {

        Channel channel = channelRepository.findOneById(id).orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));


        DResponseChannel result = modelMapper.map(channel, DResponseChannel.class);

        // only show official member
        result.setChannelMembers(result.getChannelMembers().stream().filter(
                m -> m.getStatus() == RequestType.ACCEPTED
        ).toList());

        return result;
    }

    @Override
    @Transactional
    public DResponseChannelMember getChannelRelation(Integer channelId) {

        // fetch user
        User user = userService.getOwner();

        // check channel
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found"));

        ChannelMember channelMember = channelMemberRepository.findByChannelAndMember(channel, user).orElse(null);
        if (channelMember == null)
            return null;

        return modelMapper.map(channelMember, DResponseChannelMember.class);
    }

    @Override
    @Transactional
    public void createChannel(CRequestChannel requestChannel) {

        // model map
        Channel channel = modelMapper.map(requestChannel, Channel.class);
        // get user
        User owner = userService.getOwner();

        // set normal field
        channel.setCreatedBy(owner);
        channel.setCreatedDate(LocalDateTime.now());
        channel.setModifiedDate(LocalDateTime.now());
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

        // add creator to member list
        List<CRequestChannelMember> memberList = requestChannel.getChannelMembersId();
        memberList.add(CRequestChannelMember.builder()
                .memberId(owner.getId())
                .channelPermission(PermissionAccessType.MODIFY)
                .messagePermission(PermissionAccessType.MODIFY)
                .memberPermission(PermissionAccessType.MODIFY)
                .build());

        memberList.forEach(request -> {

            // corresponding user
            User user = userRepository.findById(request.getMemberId())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel member with id " + request.getMemberId() + " is not found"));

            channelMemberRepository.save(ChannelMember.builder()
                    .member(user)
                    .channel(finalChannel)
                    .status(RequestType.ACCEPTED)
                    .channelPermission(PermissionAccessType.VIEW)
                    .memberPermission(PermissionAccessType.VIEW)
                    .messagePermission(PermissionAccessType.CREATE)
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

        // evict personal and global cache
        redisService.evictKey("channels", channelId.toString());
        redisService.evictKeysByPrefix("channels", "page-");
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

        // evict personal and global cache
        redisService.evictKey("channels", channelId.toString());
        redisService.evictKeysByPrefix("channels", "page-");
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

        // evict global cache
        redisService.evictKeysByPrefix("channels", "page-");
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

        // fetch old request
        ChannelMember channelMember = channelMemberRepository.findByChannelAndMember(channel, member).orElse(null);
        if (channelMember != null && channelMember.getStatus() != RequestType.REJECTED) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Old member request's existed.");
        }


        if (channelMember == null)
            channelMember = (ChannelMember.builder()
                    .channel(channel)
                    .member(member)
                    .channelPermission(PermissionAccessType.VIEW)
                    .memberPermission(PermissionAccessType.VIEW)
                    .messagePermission(PermissionAccessType.CREATE)
                    .joinedDate(LocalDate.now())
                    .build());
        channelMember.setStatus(RequestType.PENDING);

        channelMemberRepository.save(channelMember);

        // create notification and send socket message
        notificationService.channelRequestSend(member, channel);
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

        // create notification and send socket message
        notificationService.channelRequestAccepted(channelMember.getChannel(), channelMember.getMember());

        // evict personal and global cache
        redisService.evictKey("channels", channelId.toString());
        redisService.evictKeysByPrefix("channels", "page-");

        // send socket message to group member
        messagingTemplate.convertAndSend("/location/" + channelId, new SocketMessage(
                SocketMessage.MessageType.JOIN,
                modelMapper.map(channelMember.getMember(), DResponseUser.class)));
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

        // seen all related notification
        notificationService.seenByDestination("/channel/" + channelId + "/member/" + memberId);
    }

    @Override
    @Transactional
    public void updateMemberPermission(Integer channelId, Integer memberId, URequestChannelMember requestChannel) {

        ChannelMember channelMember = findMemberRequest(channelId, memberId);

        // change permission
        if (channelMember.getStatus() != RequestType.ACCEPTED)
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is not a member.");
        if (Objects.equals(channelMember.getChannel().getCreatedBy().getId(), memberId))
            throw new HttpException(HttpStatus.BAD_REQUEST, "Can't change the channel creator's permission.");
        channelMember.setChannelPermission(requestChannel.getChannelPermission());
        channelMember.setMessagePermission(requestChannel.getMessagePermission());
        channelMember.setMemberPermission(requestChannel.getMemberPermission());

        channelMemberRepository.save(channelMember);

        // evict personal cache
        redisService.evictKey("channels", channelId.toString());
    }

    @Override
    @Transactional
    public void kickMember(Integer channelId, Integer memberId) {
        ChannelMember channelMember = findMemberRequest(channelId, memberId);

        // change permission
        if (channelMember.getStatus() != RequestType.ACCEPTED)
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is not a member.");
        channelMember.setStatus(RequestType.REJECTED);

        channelMemberRepository.save(channelMember);

        // evict personal cache
        redisService.evictKey("channels", channelId.toString());
    }

    @Override
    @Transactional
    public void leaveChannel(Integer channelId) {

        ChannelMember channelMember = findMemberRequest(channelId, userService.getOwner().getId());

        // change request status
        if (channelMember.getStatus() != RequestType.ACCEPTED)
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is not a member.");
        channelMember.setStatus(RequestType.REJECTED);

        channelMemberRepository.save(channelMember);

        // evict personal and global cache
        redisService.evictKeysByPrefix("channels", "page-");

        // send notification to group member
        messagingTemplate.convertAndSend("/location/" + channelId + "/notification", new SocketMessage(
                SocketMessage.MessageType.LEAVE,
                modelMapper.map(channelMember.getMember(), DResponseUser.class)));
    }

    @Override
    public ChannelMember findMemberRequest(Integer channelId, Integer userId) {

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
