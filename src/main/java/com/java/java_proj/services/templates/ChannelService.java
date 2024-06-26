package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannelMember;
import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.dto.response.fordetail.DResponseChannelMember;
import com.java.java_proj.dto.response.forlist.LResponseChannel;
import com.java.java_proj.entities.ChannelMember;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ChannelService {

    Page<LResponseChannel> getChannelList(String name, Integer pageNo, Integer pageSize,
                                          String orderBy, String orderDirection);

    Page<LResponseChannel> getPersonalChannelList(String name, Integer pageNo, Integer pageSize);

    DResponseChannel getChannelProfile(Integer channelId);

    DResponseChannelMember getChannelRelation(Integer channelId);

    public void createChannel(CRequestChannel requestChannel);

    public void updateChannelProfile(Integer channelId, URequestChannel requestChannel);

    public void updateChannelAvatar(Integer channelId, MultipartFile file);

    public void disableChannel(Integer channelId);

    public void createChannelRequest(Integer channelId);

    public void acceptChannelRequest(Integer channelId, Integer memberId);

    public void rejectChannelRequest(Integer channelId, Integer memberId);

    public void updateMemberPermission(Integer channelId, Integer memberId, URequestChannelMember requestChannel);

    public void kickMember(Integer channelId, Integer memberId);

    public void leaveChannel(Integer channelId);

    public ChannelMember findMemberRequest(Integer channelId, Integer userId);

}
