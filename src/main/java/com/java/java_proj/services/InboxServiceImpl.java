package com.java.java_proj.services;

import com.java.java_proj.dto.request.forupdate.URequestInbox;
import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.dto.response.forlist.LResponseChatSpace;
import com.java.java_proj.dto.response.forlist.LResponseMessage;
import com.java.java_proj.entities.*;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.services.templates.InboxService;
import com.java.java_proj.services.templates.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InboxServiceImpl implements InboxService {

    final private InboxRepository inboxRepository;
    final private MessageRepository messageRepository;
    final private UserService userService;
    final private ModelMapper modelMapper;

    @Autowired
    public InboxServiceImpl(InboxRepository inboxRepository, MessageRepository messageRepository, UserService userService, ModelMapper modelMapper) {
        this.inboxRepository = inboxRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Page<LResponseChatSpace> getInboxList(String name, Integer page, Integer size) {

        // create pageable
        Pageable paging = PageRequest.of(page, size);

        // fetch entity page
        User user = userService.getOwner();
        Page<Inbox> inboxPage = inboxRepository.findByNameAndUser(name, user, paging);

        // map to dto
        return inboxPage.map(entity -> {
            // map to dto
            LResponseChatSpace inbox = modelMapper.map(entity, LResponseChatSpace.class);

            // convert missing fields
            ProjectionFactory pf = new SpelAwareProxyProjectionFactory();

            // fetch top message and skip the pageable part
            List<Message> messageList = messageRepository.findByInbox("", entity, PageRequest.of(0, 1))
                    .getContent();
            if (!messageList.isEmpty()) {
                inbox.setLatestMessage(pf.createProjection(LResponseMessage.class, messageList.get(0)));
            }

            // get opposite's avatar as inbox's avatar
            Resource avatar = entity.getFriendship().getSender() == userService.getOwner() ?
                    entity.getFriendship().getSender().getAvatar()
                    : entity.getFriendship().getReceiver().getAvatar();

            if (avatar != null)
                inbox.setAvatar(pf.createProjection(DResponseResource.class, avatar));

            return inbox;
        });
    }

    @Override
    @Transactional
    public LResponseChatSpace getInboxProfile(Integer inboxId) {

        // get entity
        Inbox inbox = inboxRepository.findById(inboxId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Inbox not found."));

        // map to dto
        LResponseChatSpace result = modelMapper.map(inbox, LResponseChatSpace.class);

        // get opposite's avatar as inbox's avatar
        Resource avatar = inbox.getFriendship().getSender() == userService.getOwner() ?
                inbox.getFriendship().getSender().getAvatar()
                : inbox.getFriendship().getReceiver().getAvatar();

        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        result.setAvatar(pf.createProjection(DResponseResource.class, avatar));

        return result;
    }

    @Override
    @Transactional
    public void createInbox(Integer userId) {

        // find existing friendship
        UserFriendship friendship = userService.findFriendship(userId);

        // count active inbox
        if (inboxRepository.countByFriendshipAndIsActive(friendship, true) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Inbox is already exist");
        }

        Inbox inbox = Inbox.builder()
                .name("Chat between " + friendship.getSender().getName() +
                        " and " + friendship.getReceiver().getName())
                .friendship(friendship)
                .senderLastSeen(null)
                .receiverLastSeen(null)
                .isActive(true)
                .build();

        inboxRepository.save(inbox);
    }

    @Override
    public void updateInboxProfile(Integer userId, URequestInbox requestInbox) {

        // find active inbox
        Inbox inbox = inboxRepository.findByFriendshipAndIsActive(userService.findFriendship(userId), true)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Inbox not found."));

        inbox.setName(requestInbox.getName());
        inbox.setIsActive(requestInbox.getIsActive());

        inboxRepository.save(inbox);
    }

}
