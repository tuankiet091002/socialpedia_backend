package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestChannel;
import com.java.java_proj.dto.request.forcreate.CRequestChannelMember;
import com.java.java_proj.dto.request.forupdate.URequestChannel;
import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.dto.response.forlist.LResponseChannel;
import com.java.java_proj.entities.Channel;
import com.java.java_proj.entities.ChannelMember;
import com.java.java_proj.entities.Resource;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.miscs.CustomUserDetail;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.ChannelService;
import com.java.java_proj.services.templates.ResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ResourceService resourceService;
    @Autowired
    ModelMapper modelMapper;

    private User getOwner() {
        try {
            return ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public Page<LResponseChannel> getAllChannel(String name, Integer page, Integer size, String orderBy, String orderDirection) {
        // create pageable
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(page, size, Sort.by(orderBy).ascending())
                : PageRequest.of(page, size, Sort.by(orderBy).descending());

        // fetch entity page
        Page<Channel> channelPage = channelRepository.findByNameContaining(name, paging);

        // map to dto
        return channelPage.map(entity -> {
            // map to dto
            LResponseChannel channel = modelMapper.map(entity, LResponseChannel.class);

            // fetch latest message and set
            DResponseMessage latestMessage = messageRepository.findTopByChannelOrderByCreatedDateDesc(entity);
            channel.setLatestMessage(latestMessage);

            return channel;
        });
    }

    @Override
    public DResponseChannel getOneChannel(Integer id) {

        // get channel entity and map it to dto
        Channel channel = channelRepository.findOneById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));
        DResponseChannel responseChannel = modelMapper.map(channel, DResponseChannel.class);

        // fetch latest message and set
        DResponseMessage latestMessage = messageRepository.findTopByChannelOrderByCreatedDateDesc(channel);
        responseChannel.setLatestMessage(latestMessage);

        return responseChannel;
    }

    @Override
    public DResponseChannel createChannel(CRequestChannel requestChannel) {

        Channel channel = modelMapper.map(requestChannel, Channel.class);

        // avatar file check
        if (requestChannel.getAvatarFile() != null) {
            Resource resource = resourceService.addFile(requestChannel.getAvatarFile());
            channel.setAvatar(resource);
        }

        channel.setCreatedBy(null);
        channel.setCreatedDate(LocalDate.now());

        channel = channelRepository.save(channel);

        //set member list after save once
        channel.setChannelMembers(genChannelMemberList(channel.getId(),
                requestChannel.getChannelMembersId()));

        channelRepository.save(channel);
        return getOneChannel(channel.getId());
    }

    @Override
    public DResponseChannel updateChannel(URequestChannel requestChannel) {


        Channel channel = channelRepository.findById(requestChannel.getId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found"));

        channel.setName(requestChannel.getName());
        channel.setDescription(requestChannel.getDescription());

        // set member list
        channel.setChannelMembers(genChannelMemberList(channel.getId(),
                requestChannel.getChannelMembersId()));

        channel.setModifiedBy(getOwner());
        channel.setModifiedDate(LocalDate.now());

        channel = channelRepository.save(channel);
        return getOneChannel(channel.getId());
    }

    @Override
    public void deleteChannel(Integer id) {

        if (channelRepository.countById(id) == 0)
            throw new HttpException(HttpStatus.NOT_FOUND, "Channel not found");

        channelRepository.deleteById(id);
    }

    private List<ChannelMember> genChannelMemberList(Integer channelId, List<CRequestChannelMember> requestChannelList) {

        return requestChannelList.stream()
                .map(request -> {

                    // dummy channel
                    Channel dummy = new Channel();
                    dummy.setId(channelId);

                    // corresponding user
                    User user = userRepository.findById(request.getMemberId())
                            .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel member with id " + request.getMemberId() + " is not found"));

                    return new ChannelMember(dummy, user, request.getPermission());
                })
                .collect(Collectors.toList());
    }
}
