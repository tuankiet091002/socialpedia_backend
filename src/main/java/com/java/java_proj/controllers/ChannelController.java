package com.java.java_proj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.java_proj.dto.request.forcreate.CRequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannelMember;
import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.dto.response.fordetail.DResponseChannelMember;
import com.java.java_proj.dto.response.forlist.LResponseChannel;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.ChannelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/channel")
public class ChannelController {

    final private ChannelService channelService;
    final private ObjectMapper objectMapper;
    final private Validator validator;

    @Autowired
    public ChannelController(ChannelService channelService, ObjectMapper objectMapper, Validator validator) {
        this.channelService = channelService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }


    @GetMapping("")
    @PreAuthorize("hasPermission('GLOBAL', 'CHANNEL', 'VIEW')")
    public ResponseEntity<Page<LResponseChannel>>
    getChannelList(@RequestParam(value = "name", defaultValue = "") String name,
                   @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                   @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                   @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
                   @RequestParam(value = "orderDirection", defaultValue = "DESC") String orderDirection) {

        List<String> allowedFields = Arrays.asList("id", "name", "createdBy", "createdDate", "modifiedDate");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        List<String> allowedSort = Arrays.asList("ASC", "DESC");
        if (!allowedSort.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Sort Direction " + orderDirection + " is illegal!");
        }

        Page<LResponseChannel> channelPage = channelService.getChannelList(name, page, size, orderBy, orderDirection);

        return new ResponseEntity<>(channelPage, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/self")
    public ResponseEntity<Page<LResponseChannel>>
    getPersonalChannelList(@RequestParam(value = "name", defaultValue = "") String name,
                           @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                           @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {

        Page<LResponseChannel> channelPage = channelService.getPersonalChannelList(name, page, size);

        return new ResponseEntity<>(channelPage, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<DResponseChannel> getChannelProfile(@PathVariable Integer channelId) {

        DResponseChannel channel = channelService.getChannelProfile(channelId);

        return new ResponseEntity<>(channel, HttpStatus.OK);
    }

    @GetMapping("/{channelId}/relation")
    public ResponseEntity<DResponseChannelMember> getChannelRelation(@PathVariable Integer channelId) {

        DResponseChannelMember channelMember = channelService.getChannelRelation(channelId);

        return new ResponseEntity<>(channelMember, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Null> createChannel(@RequestPart String content,
                                              @RequestPart(required = false) MultipartFile file) throws JsonProcessingException {

        CRequestChannel requestChannel = objectMapper.readValue(content, CRequestChannel.class);

        // get validation error
        DataBinder binder = new DataBinder(requestChannel);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        // set avatar if exist
        if (file == null || !file.isEmpty()) {
            requestChannel.setAvatarFile(file);
        }

        channelService.createChannel(requestChannel);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{channelId}/profile")
    @PreAuthorize("hasPermission(#channelId, 'CHANNEL', 'MODIFY')")
    public ResponseEntity<Null> updateChannelProfile(
            @PathVariable Integer channelId,
            @Valid @RequestBody URequestChannel requestChannel, BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        channelService.updateChannelProfile(channelId, requestChannel);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{channelId}/avatar")
    @PreAuthorize("hasPermission(#channelId, 'CHANNEL', 'MODIFY')")
    public ResponseEntity<Null> updateChannelAvatar(@PathVariable Integer channelId, @RequestPart MultipartFile file) {

        channelService.updateChannelAvatar(channelId, file);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{channelId}")
    @PreAuthorize("hasPermission('GLOBAL', 'CHANNEL', 'MODIFY') " +
            "or hasPermission(#channelId, 'CHANNEL', 'MODIFY')")
    public ResponseEntity<Null> disableChannel(@PathVariable Integer channelId) {

        channelService.disableChannel(channelId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/{channelId}/member")
    public ResponseEntity<Null> createChannelRequest(@PathVariable Integer channelId) {

        channelService.createChannelRequest(channelId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{channelId}/member/{memberId}/accept")
    @PreAuthorize("hasPermission(#channelId, 'CHANNEL-MEMBER', 'CREATE')")
    public ResponseEntity<Null> acceptChannelRequest(@PathVariable Integer channelId,
                                                     @PathVariable Integer memberId) {

        channelService.acceptChannelRequest(channelId, memberId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{channelId}/member/{memberId}/reject")
    @PreAuthorize("hasPermission(#channelId, 'CHANNEL-MEMBER', 'CREATE')")
    public ResponseEntity<Null> rejectChannelRequest(@PathVariable Integer channelId,
                                                     @PathVariable Integer memberId) {

        channelService.rejectChannelRequest(channelId, memberId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{channelId}/member/{memberId}")
    @PreAuthorize("hasPermission(#channelId, 'CHANNEL-MEMBER', 'MODIFY')")
    public ResponseEntity<Null> updateMemberPermission(@PathVariable Integer channelId,
                                                       @PathVariable Integer memberId,
                                                       @Valid @RequestBody URequestChannelMember requestChannel,
                                                       BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        channelService.updateMemberPermission(channelId, memberId, requestChannel);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{channelId}/member/{memberId}")
    @PreAuthorize("hasPermission(#channelId, 'CHANNEL-MEMBER', 'MODIFY')")
    public ResponseEntity<Null> kickMember(@PathVariable Integer channelId,
                                           @PathVariable Integer memberId) {

        channelService.kickMember(channelId, memberId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @DeleteMapping("/{channelId}/member")
    @PreAuthorize("hasPermission(#channelId, 'CHANNEL-MEMBER', 'SELF')")
    public ResponseEntity<Null> leaveChannel(@PathVariable Integer channelId) {

        channelService.leaveChannel(channelId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
