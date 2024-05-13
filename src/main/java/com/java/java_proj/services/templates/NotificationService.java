package com.java.java_proj.services.templates;

import com.java.java_proj.dto.response.forlist.LResponseNotification;
import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.User;
import org.springframework.data.domain.Page;

public interface NotificationService {

    public Page<LResponseNotification> getNotificationList(Integer page, Integer size);

    public void friendRequestSend(User source, User target);

    public void friendRequestAccepted(User source, User target);

    public void channelRequestSend(User source, Channel channel);

    public void channelRequestAccepted(Channel channel, User target);

    public void messageToChannel(Integer channelId);

    public void messageToInbox(Integer inboxId);

    public void seenByDestination(String destination);

    public void seenByUserAndDestination(User user, String destination);

}
