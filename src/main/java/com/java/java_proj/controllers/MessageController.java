package com.java.java_proj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.java_proj.dto.request.forcreate.CRequestMessage;
import com.java.java_proj.dto.request.forupdate.URequestMessageProfile;
import com.java.java_proj.dto.request.forupdate.URequestMessageStatus;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    final private MessageService messageService;
    final private ObjectMapper objectMapper;
    final private Validator validator;

    @Autowired
    public MessageController(MessageService messageService, ObjectMapper objectMapper, Validator validator) {
        this.messageService = messageService;
        this.objectMapper = objectMapper;
        this.validator = validator;
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
    @PreAuthorize("hasPermission(#channelId, 'CHANNEL-MESSAGE', 'CREATE')")
    public ResponseEntity<Null> sendMessageToChannel(@PathVariable Integer channelId, @RequestPart String content,
                                                     @RequestPart(required = false) List<MultipartFile> files) throws JsonProcessingException {

        CRequestMessage requestMessage = objectMapper.readValue(content, CRequestMessage.class);

        // get validation error
        DataBinder binder = new DataBinder(requestMessage);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        // check if file list is empty
        if (files == null || (files.size() == 1 && files.get(0).isEmpty())) {
            requestMessage.setResourceFiles(new ArrayList<>());
        } else {
            requestMessage.setResourceFiles(files);
        }

        messageService.sendMessageToChannel(channelId, requestMessage);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/inbox/{inboxId}")
    @PreAuthorize("hasPermission(#inboxId, 'INBOX', 'CREATE')")
    public ResponseEntity<Null> sendMessageToInbox(@PathVariable Integer inboxId, @RequestPart String content,
                                                   @RequestPart(required = false) List<MultipartFile> files) throws JsonProcessingException {

        CRequestMessage requestMessage = objectMapper.readValue(content, CRequestMessage.class);

        // get validation error
        DataBinder binder = new DataBinder(requestMessage);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        // check if file list is empty
        if (files == null || (files.size() == 1 && files.get(0).isEmpty())) {
            requestMessage.setResourceFiles(new ArrayList<>());
        } else {
            requestMessage.setResourceFiles(files);
        }

        messageService.sendMessageToInbox(inboxId, requestMessage);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{locationId}/content")
    @PreAuthorize("hasPermission(#locationId, 'INBOX', 'CREATE') " +
            " or hasPermission(#locationId, 'CHANNEL-MESSAGE', 'CREATE')")
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
            " or hasPermission(#locationId, 'CHANNEL-MESSAGE', 'MODIFY')")
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
    @PreAuthorize("hasPermission(#locationId, 'INBOX', 'MODIFY') " +
            " or hasPermission(#locationId, 'CHANNEL-MESSAGE', 'MODIFY')")
    // delete self
    public ResponseEntity<Null> deleteMessage(@PathVariable Integer locationId,
                                              @PathVariable Integer messageId) {

        messageService.deleteMessage(locationId, messageId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
