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

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // prevent circled dependency
    private User getOwner() {
        try {
            return ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        } catch (Exception e) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Current user not found.");
        }

    }

    @Override
    public Page<LResponseNotification> getNotificationList(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        return notificationRepository.findByUserOrderByIdDesc(getOwner(), pageable);
    }

    @Override
    public void friendRequestSend(User source, User target) {

        Notification notification = Notification.builder()
                .user(target)
                .avatar(source.getAvatar())
                .title("Lời mời kết bạn mới")
                .content(String.format("%s đã gửi cho bạn lời mời kết bạn", source.getName()))
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
                .title("Kết bạn thành công")
                .content(source.getName() + " đã đồng ý lời mời kết bạn")
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
                        .title("Có lời mời vào nhóm")
                        .content(source.getName() + " đã gửi lời mời vào nhóm " + channel.getName() + ".")
                        .destination("/channel/" + channel.getId() + "/member/" + channelMember.getMember().getId())
                        .type(NotificationType.REQUEST)
                        .createdDate(LocalDateTime.now())
                        .build());

                messagingTemplate.convertAndSend("/user/" + channelMember.getMember().getId() + "/notification", new SocketMessage());
            }
        });
    }

    @Override
    public void channelRequestAccepted(Channel channel, User target) {

        Notification notification = Notification.builder()
                .user(target)
                .avatar(channel.getAvatar())
                .title("Vào nhóm thành công")
                .content("Yêu cầu tham gia vào nhóm " + channel.getName() + " của bạn đã được đồng ý.")
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
    public void seenById(Integer id) {

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
