package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestMessage;
import com.java.java_proj.dto.request.forupdate.URequestMessage;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import org.springframework.data.domain.Page;

public interface MessageService {

    Page<DResponseMessage> getAllMessageByChannel(String content, Integer channelId, Integer page, Integer size);

    DResponseMessage getOneMessage(Integer channelId);

    public DResponseMessage createMessage(CRequestMessage message);

    public DResponseMessage updateMessage(URequestMessage message);

    public void deleteMessage(Integer id);
}

