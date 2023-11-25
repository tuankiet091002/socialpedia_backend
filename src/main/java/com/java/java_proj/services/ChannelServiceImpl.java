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
import com.java.java_proj.entities.miscs.ChannelMemberCompositeKey;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.ChannelMemberRepository;
import com.java.java_proj.repositories.ChannelRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.ChannelService;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ChannelMemberRepository channelMemberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ResourceService resourceService;
    @Autowired
    UserService userService;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public Page<LResponseChannel> getAllChannel(String name, Integer page, Integer size, String orderBy, String orderDirection) {

        // fetch user
        User user = userService.getOwner();

        // create pageable
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(page, size, Sort.by(orderBy).ascending())
                : PageRequest.of(page, size, Sort.by(orderBy).descending());

        // fetch entity page
        Page<Channel> channelPage = channelRepository.findChannelIn(name, user, paging);

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

        return channelRepository.findOneById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));
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

        channel.setModifiedBy(userService.getOwner());
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

    @Override
    public void joinChannel(Integer channelId) {

        // fetch channel from id
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));

        // fetch user from session
        User user = userService.getOwner();

        // check if exist
        if (channelMemberRepository.countByChannelAndMember(channel, user) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is already in the channel");
        }
        ChannelMember channelMember = new ChannelMember(channel, user);

        channelMemberRepository.save(channelMember);
    }

    @Override
    public void leaveChannel(Integer channelId) {
        // fetch channel from id
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Channel not found."));

        // fetch user from session
        User user = userService.getOwner();

        // check if exist
        if (channelMemberRepository.countByChannelAndMember(channel, user) == 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is not in the channel");
        }

        channelMemberRepository.deleteById(new ChannelMemberCompositeKey(channel, user));
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

                    return new ChannelMember(dummy, user, request.getChatPermission(), request.getMemberPermission());
                })
                .collect(Collectors.toList());
    }
}
