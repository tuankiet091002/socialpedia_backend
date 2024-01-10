package com.java.java_proj.services;

import com.java.java_proj.dto.response.forlist.LResponseNotification;
import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.Notification;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.enums.PermissionAccessType;
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
                .title("Lời mời kết bạn mới đã gửi lời mời kết bạn")
                .content(source.getName() + " đã gửi lời mời kết bạn.")
                .target("user/" + source.getEmail())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/user/" + target.getId(), "");
    }

    @Override
    public void friendRequestAccepted(User source, User target) {

        Notification notification = Notification.builder()
                .user(target)
                .avatar(source.getAvatar())
                .title("Kết bạn thành công")
                .content(source.getName() + " đã đồng ý lời mời kết bạn.")
                .target("user/" + source.getEmail())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/user/" + target.getId(), "");
    }

    @Override
    @Transactional
    public void channelRequestSend(User source, Channel channel) {

        channel.getChannelMembers().forEach(channelMember -> {
            if (channelMember.getMemberPermission().getValue() >= PermissionAccessType.CREATE.getValue())

                notificationRepository.save(Notification.builder()
                        .user(channelMember.getMember())
                        .avatar(source.getAvatar())
                        .title("Có lời mời vào nhóm")
                        .content(source.getName() + " đã gửi lời mời vào nhóm " + channel.getName() + ".")
                        .target("channel/" + channel.getId())
                        .isRead(false)
                        .build());

            messagingTemplate.convertAndSend("/user/" + channelMember.getMember().getId(), channelMember.getMember().getName());

        });
    }

    @Override
    public void channelRequestAccepted(Channel channel, User target) {

        Notification notification = Notification.builder()
                .user(target)
                .avatar(channel.getAvatar())
                .title("Yêu cầu tham gia vào nhóm đã được chấp nhận")
                .content("Yêu cầu tham gia" + channel.getName() + " đã đồng ý lời mời kết bạn.")
                .target("channel/" + channel.getId())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/user/" + target.getId(), target.getName());
    }

    @Override
    public void messageToChannel(Integer channelId) {
        messagingTemplate.convertAndSend("/space/" + channelId,
                new SocketMessage(SocketMessage.MessageType.CHAT, ""));
    }

    @Override
    public void messageToInbox(Integer inboxId) {
        messagingTemplate.convertAndSend("/space/" + inboxId,
                new SocketMessage(SocketMessage.MessageType.CHAT, ""));
    }

}
