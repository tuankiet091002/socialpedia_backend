package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forcreate.CRequestMessage;
import com.java.java_proj.dto.request.forupdate.URequestMessageProfile;
import com.java.java_proj.dto.request.forupdate.URequestMessageStatus;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.entities.miscs.SocketMessage;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    final private MessageService messageService;
    final private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/channel/{channelId}")
    @PreAuthorize("hasPermission(#channelId, 'CHANNEL', 'VIEW')")
    public ResponseEntity<Page<DResponseMessage>> getMessagesFromChannel(@PathVariable Integer channelId,
                                                                         @RequestParam(value = "content", defaultValue = "") String content,
                                                                         @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                                                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {


        Page<DResponseMessage> messagePage = messageService.getMessagesFromChannel(channelId, content, page, size);

        return new ResponseEntity<>(messagePage, HttpStatus.OK);
    }

    @GetMapping("/inbox/{inboxId}")
    @PreAuthorize("hasPermission(#inboxId, 'INBOX', 'VIEW')")
    public ResponseEntity<Page<DResponseMessage>> getMessagesFromInbox(@PathVariable Integer inboxId,
                                                                       @RequestParam(value = "content", defaultValue = "") String content,
                                                                       @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {


        Page<DResponseMessage> messagePage = messageService.getMessagesFromInbox(inboxId, content, page, size);

        return new ResponseEntity<>(messagePage, HttpStatus.OK);
    }

    @PostMapping("/channel/{channelId}")
    @PreAuthorize("hasPermission(#channelId, 'CHANNEL', 'CREATE')")
    public ResponseEntity<Null> sendMessageToChannel(@PathVariable Integer channelId,
                                                     @Valid @RequestBody CRequestMessage requestMessage,
                                                     BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        messageService.sendMessageToChannel(channelId, requestMessage);

        // socket message for member in the same channel
        messagingTemplate.convertAndSend("/channel/" + channelId,
                new SocketMessage(SocketMessage.MessageType.CHAT,
                        "", channelId.toString()));

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/inbox/{inboxId}")
    @PreAuthorize("hasPermission(#inboxId, 'INBOX', 'CREATE')")
    public ResponseEntity<Null> sendMessageToInbox(@PathVariable Integer inboxId,
                                                   @Valid @RequestBody CRequestMessage requestMessage,
                                                   BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        messageService.sendMessageToInbox(inboxId, requestMessage);

        // socket message for member in the same inbox
        messagingTemplate.convertAndSend("/inbox/" + inboxId,
                new SocketMessage(SocketMessage.MessageType.CHAT,
                        "", inboxId.toString()));

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{locationId}/content")
    @PreAuthorize("hasPermission(#locationId, 'INBOX', 'CREATE') " +
            " or hasPermission(#locationId, 'CHANNEL', 'CREATE')")
    public ResponseEntity<Null> updateMessageContent(@PathVariable Integer locationId,
                                                     @Valid @RequestBody URequestMessageProfile requestMessage,
                                                     BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        messageService.updateMessageContent(locationId, requestMessage);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{locationId}/status")
    @PreAuthorize("hasPermission(#locationId, 'INBOX', 'MODIFY') " +
            " or hasPermission(#locationId, 'CHANNEL', 'MODIFY')")
    public ResponseEntity<Null> updateMessageStatus(@PathVariable Integer locationId,
                                                    @Valid @RequestBody URequestMessageStatus requestMessage,
                                                    BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        messageService.updateMessageStatus(locationId, requestMessage);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{locationId}/{messageId}")
    @PreAuthorize("hasPermission(#locationId, 'INBOX', 'CREATE') " +
            " or hasPermission(#locationId, 'CHANNEL', 'CREATE')")
    // delete self
    public ResponseEntity<Null> deleteMessage(@PathVariable Integer locationId,
                                              @PathVariable Integer messageId) {

        messageService.deleteMessage(locationId, messageId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
