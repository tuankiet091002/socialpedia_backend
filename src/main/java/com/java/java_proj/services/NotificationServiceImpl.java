package com.java.java_proj.services;

import com.java.java_proj.dto.response.forlist.LResponseNotification;
import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.Notification;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.enums.NotificationType;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.entities.miscs.CustomUserDetail;
import com.java.java_proj.entities.miscs.SocketMessage;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.NotificationRepository;
import com.java.java_proj.services.templates.NotificationService;
import jakarta.transaction.Transactional;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToUrl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {

    final private NotificationRepository notificationRepository;
    final private SimpMessagingTemplate messagingTemplate;
    final private ModelMapper modelMapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate, ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
        this.modelMapper = modelMapper;
    }

    // prevent circled dependency
    public User getOwner() {
        try {
            return ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        } catch (Exception e) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Current user not found.");
        }

    }

    @Override
    public Page<LResponseNotification> getNotificationList(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        return notificationRepository.findByUserOrderByIdDesc(getOwner(), pageable).map(notification -> modelMapper.map(notification, LResponseNotification.class));
    }

    @Override
    public void friendRequestSend(User source, User target) {

        Notification notification = Notification.builder()
                .user(target)
                .avatar(source.getAvatar())
                .title("New friend request")
                .content(String.format("%s has sent you a friend request.", source.getName()))
                .destination("/user/" + source.getId())
                .type(NotificationType.REQUEST)
                .createdDate(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/user/" + target.getId() + "/notification", new SocketMessage());
    }

    @Override
    public void friendRequestAccepted(User source, User target) {

        Notification notification = Notification.builder()
                .user(target)
                .avatar(source.getAvatar())
                .title("Friend request accepted")
                .content(source.getName() + " has accepted your friend request.")
                .destination("/user/" + source.getId())
                .type(NotificationType.VIEW)
                .createdDate(LocalDateTime.now())
                .build();

        // seen all related message
        seenByUserAndDestination(source, "/user/" + target.getId());

        // create new notification
        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/user/" + target.getId() + "/notification", new SocketMessage());
    }

    @Override
    @Transactional
    public void channelRequestSend(User source, Channel channel) {

        channel.getChannelMembers().forEach(channelMember -> {

            // loop all members with appropriate permission
            if (channelMember.getMemberPermission().getValue() >= PermissionAccessType.CREATE.getValue() && channelMember.getStatus() == RequestType.ACCEPTED) {

                // send notification
                notificationRepository.save(Notification.builder()
                        .user(channelMember.getMember())
                        .avatar(source.getAvatar())
                        .title("Join request")
                        .content(source.getName() + " want to join " + channel.getName() + ".")
                        .destination("/channel/" + channel.getId() + "/member/" + source.getId())
                        .type(NotificationType.REQUEST)
                        .createdDate(LocalDateTime.now())
                        .build());

                // refresh device notification list
                messagingTemplate.convertAndSend("/user/" + channelMember.getMember().getId() + "/notification", new SocketMessage());
            }
        });
    }

    @Override
    public void channelRequestAccepted(Channel channel, User target) {

        Notification notification = Notification.builder()
                .user(target)
                .avatar(channel.getAvatar())
                .title("Join request accepted")
                .content("You are now a member of channel " + channel.getName() + ".")
                .destination("/channel/" + channel.getId())
                .type(NotificationType.VIEW)
                .createdDate(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        seenByDestination("/channel/" + channel.getId() + "/member/" + target.getId());

        messagingTemplate.convertAndSend("/user/" + target.getId() + "/notification", new SocketMessage());
    }

    @Override
    public void messageToChannel(Integer channelId) {
        messagingTemplate.convertAndSend("/space/" + channelId,
                new SocketMessage(SocketMessage.MessageType.CHAT, null));
    }

    @Override
    public void messageToInbox(Integer inboxId) {
        messagingTemplate.convertAndSend("/space/" + inboxId,
                new SocketMessage(SocketMessage.MessageType.CHAT, null));
    }

    @Override
    @Transactional
    public void seenAll() {
        User owner = getOwner();
        notificationRepository.seenByUser(owner);
    }

    @Override
    public void seenByDestination(String destination) {
        notificationRepository.seenByDestination(destination);
    }

    @Override
    public void seenByUserAndDestination(User user, String destination) {
        notificationRepository.seenByUserAndDestination(user, destination);
    }
}
