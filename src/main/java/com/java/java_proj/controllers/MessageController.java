package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forcreate.CRequestMessage;
import com.java.java_proj.dto.request.forupdate.URequestMessage;
import com.java.java_proj.dto.response.fordetail.DResponseMessage;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.MessageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;

@RestController
@RequestMapping("/message")
@Api(tags = "Message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("")
    public ResponseEntity<Page<DResponseMessage>> getAllMessage(@RequestParam(value = "channel") Integer channelId,
                                                                @RequestParam(value = "content", defaultValue = "") String content,
                                                                @RequestParam(value = "page-no", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "page-size", defaultValue = "10") Integer size) {


        Page<DResponseMessage> messagePage = messageService.getAllMessageByChannel(content, channelId, page, size);

        if (messagePage.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Currently no records.");
        }

        return new ResponseEntity<>(messagePage, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<DResponseMessage> createMessage(@Valid @RequestBody CRequestMessage requestMessage,
                                                          BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseMessage message = messageService.createMessage(requestMessage);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<DResponseMessage> updateMessage(@Valid @RequestBody URequestMessage requestMessage,
                                                          BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseMessage message = messageService.updateMessage(requestMessage);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Null> deleteMessage(@PathVariable Integer messageId) {

        messageService.deleteMessage(messageId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
