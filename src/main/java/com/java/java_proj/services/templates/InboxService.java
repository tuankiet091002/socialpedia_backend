package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forupdate.URequestInbox;
import com.java.java_proj.dto.response.fordetail.DResponseInbox;
import com.java.java_proj.dto.response.forlist.LResponseChatSpace;
import org.springframework.data.domain.Page;

public interface InboxService {

    Page<LResponseChatSpace> getInboxList(String name, Integer pageNo, Integer pageSize);

    DResponseInbox getInboxProfile(Integer inboxId);

    public void createInbox(Integer userId);

    public void updateInboxProfile(Integer userId, URequestInbox requestInbox);

}
