package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannel;
import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.dto.response.forlist.LResponseChannel;
import org.springframework.data.domain.Page;

public interface ChannelService {

    Page<LResponseChannel> getAllChannel(String name, Integer page, Integer size, String orderBy, String orderDirection);

    DResponseChannel getOneChannel(Integer id);

    public DResponseChannel createChannel(CRequestChannel channel);

    public DResponseChannel updateChannel(URequestChannel channel);

    public void deleteChannel(Integer id);
}
