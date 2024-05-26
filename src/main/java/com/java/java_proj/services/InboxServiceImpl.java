package com.java.java_proj.services;

import com.java.java_proj.dto.request.forupdate.URequestInbox;
import com.java.java_proj.dto.response.fordetail.DResponseInbox;
import com.java.java_proj.dto.response.fordetail.DResponseResource;
import com.java.java_proj.dto.response.forlist.LResponseInbox;
import com.java.java_proj.dto.response.forlist.LResponseMessage;
import com.java.java_proj.dto.response.forlist.LResponseUserMinimal;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public Page<LResponseInbox> getInboxList(String name, Integer page, Integer size) {

        // create pageable
        Pageable paging = PageRequest.of(page, size);

        // fetch entity page
        User user = userService.getOwner();
        Page<Inbox> inboxPage = inboxRepository.findByNameAndUser(name, user, paging);

        // map to dto
        return inboxPage.map(entity -> {
            // map to dto
            LResponseInbox inbox = modelMapper.map(entity, LResponseInbox.class);

            // fetch top message and skip the pageable part
            List<Message> messageList = messageRepository.findByInbox("", entity,
                    PageRequest.of(0, 1,
                            Sort.by("id").descending())).getContent();
            if (!messageList.isEmpty()) {
                inbox.setLatestMessage(modelMapper.map(messageList.get(0), LResponseMessage.class));
            }

            boolean ownerIsSender = Objects.equals(entity.getFriendship().getSender().getId(), user.getId());

            // get contact link
            inbox.setContactWith(modelMapper.map(ownerIsSender ?
                    entity.getFriendship().getReceiver() : entity.getFriendship().getSender(), LResponseUserMinimal.class));

            // get opposite's avatar as inbox's avatar
            Resource avatar = ownerIsSender ?
                    entity.getFriendship().getReceiver().getAvatar()
                    : entity.getFriendship().getSender().getAvatar();

            if (avatar != null) {
                inbox.setAvatar(modelMapper.map(avatar, DResponseResource.class));
            }

            return inbox;
        });
    }

    @Override
    @Transactional
    public DResponseInbox getInboxProfile(Integer inboxId) {

        // get entity
        Inbox inbox = inboxRepository.findById(inboxId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Inbox not found."));

        // map to dto
        DResponseInbox result = modelMapper.map(inbox, DResponseInbox.class);

        // get opposite's avatar as inbox's avatar
        Resource avatar = inbox.getFriendship().getSender() == userService.getOwner() ?
                inbox.getFriendship().getSender().getAvatar()
                : inbox.getFriendship().getReceiver().getAvatar();

        if (avatar != null) {
            result.setAvatar(modelMapper.map(avatar, DResponseResource.class));
        }

        // get opposite account's last seen message

        if (inbox.getFriendship().getSender() == userService.getOwner()) {
            if (inbox.getReceiverLastSeen() != null)
                result.setLastSeenMessageId(inbox.getReceiverLastSeen().getId());
        } else {
            if (inbox.getSenderLastSeen() != null)
                result.setLastSeenMessageId(inbox.getSenderLastSeen().getId());
        }

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
                .name(friendship.getSender().getName() + " -- " + friendship.getReceiver().getName())
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
