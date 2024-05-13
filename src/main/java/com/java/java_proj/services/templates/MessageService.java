package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestMessage;
import com.java.java_proj.dto.request.forupdate.URequestMessageProfile;
import com.java.java_proj.dto.request.forupdate.URequestMessageStatus;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import org.springframework.data.domain.Page;

public interface MessageService {

    public Page<DResponseMessage> getMessagesFromChannel(Integer channelId, String content, Integer page, Integer size);

    public Page<DResponseMessage> getMessagesFromInbox(Integer inboxId, String content, Integer page, Integer size);

    public void sendMessageToChannel(Integer locationId, CRequestMessage requestMessage);

    public void sendMessageToInbox(Integer locationId, CRequestMessage requestMessage);

    public void updateMessageContent(Integer locationId, URequestMessageProfile requestMessage);

    public void updateMessageStatus(Integer locationId, URequestMessageStatus requestMessage);

    public void deleteMessage(Integer locationId, Integer id);

    public void seeInboxMessage(Integer inboxId, Integer messageId, Integer ownerId);

}

