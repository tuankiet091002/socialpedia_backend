package com.java.java_proj.services;

import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.dto.response.forlist.LResponseChatSpace;
import com.java.java_proj.entities.Inbox;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserFriendship;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.MessageRepository;
import com.java.java_proj.services.templates.InboxService;
import com.java.java_proj.services.templates.UserService;
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
    public Page<LResponseChatSpace> getInboxList(String name, Integer page, Integer size,
                                                 String orderBy, String orderDirection) {

        // create pageable
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(page, size, Sort.by(orderBy).ascending())
                : PageRequest.of(page, size, Sort.by(orderBy).descending());

        // fetch entity page
        Page<Inbox> inboxPage = inboxRepository.findByNameContaining(name, paging);

        // map to dto
        return inboxPage.map(entity -> {
            // map to dto
            LResponseChatSpace inbox = modelMapper.map(entity, LResponseChatSpace.class);

            // fetch top message and skip the pageable part
            List<DResponseMessage> messageList = messageRepository.findByInbox("", entity, PageRequest.of(0, 1))
                    .getContent();
            if (!messageList.isEmpty()) {
                inbox.setLatestMessage(messageList.get(0));
            }

            return inbox;
        });
    }

    @Override
    public void createInbox(Integer userId) {

        // find existing friendship
        UserFriendship friendship = userService.findFriendship(userId);
        if (inboxRepository.countByFriendship(friendship) > 0) {
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
    public void disableInbox(Integer id) {

        // find inbox
        Inbox inbox = inboxRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Inbox not found."));

        // check authorization
        User owner = userService.getOwner();
        if (!Objects.equals(inbox.getFriendship().getSender().getId(), owner.getId())
                || !Objects.equals(inbox.getFriendship().getReceiver().getId(), owner.getId())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Not your inbox.");
        }
        inbox.setIsActive(false);

        inboxRepository.save(inbox);
    }
}
