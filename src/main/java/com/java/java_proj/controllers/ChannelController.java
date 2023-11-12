package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forcreate.CRequestChannel;
import com.java.java_proj.dto.request.forupdate.URequestChannel;
import com.java.java_proj.dto.response.fordetail.DResponseChannel;
import com.java.java_proj.dto.response.forlist.LResponseChannel;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.ChannelService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/channel")
@Api(tags = "Channel")
public class ChannelController {

    @Autowired
    ChannelService channelService;

    @GetMapping()
    public ResponseEntity<Page<LResponseChannel>> getAllChannel(@RequestParam(value = "name", defaultValue = "") String name,
                                                                @RequestParam(value = "order-by", defaultValue = "id") String orderBy,
                                                                @RequestParam(value = "page-no", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "page-size", defaultValue = "10") Integer size,
                                                                @RequestParam(value = "order-direction", defaultValue = "DESC") String orderDirection) {

        List<String> allowedFields = Arrays.asList("id", "name");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        List<String> allowedSort = Arrays.asList("ASC", "DESC");
        if (!allowedSort.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Sort Direction " + orderDirection + " is illegal!");
        }

        Page<LResponseChannel> channelPage = channelService.getAllChannel(name, page, size, orderBy, orderDirection);

        if (channelPage.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Currently no records.");
        }

        return new ResponseEntity<>(channelPage, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<DResponseChannel> getOneChannel(@PathVariable Integer channelId) {

        DResponseChannel channel = channelService.getOneChannel(channelId);

        return new ResponseEntity<>(channel, HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<DResponseChannel> createChannel(@Valid @RequestPart CRequestChannel requestChannel,
                                                          @RequestPart MultipartFile file,
                                                          BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }
        requestChannel.setAvatarFile(file);

        DResponseChannel channel = channelService.createChannel(requestChannel);

        return new ResponseEntity<>(channel, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<DResponseChannel> updateChannel(@Valid @RequestPart URequestChannel requestChannel,
                                                          @RequestPart MultipartFile file,
                                                          BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }
        requestChannel.setAvatarFile(file);

        DResponseChannel channel = channelService.updateChannel(requestChannel);

        return new ResponseEntity<>(channel, HttpStatus.OK);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Null> deleteChannel(@PathVariable Integer channelId) {

        channelService.deleteChannel(channelId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
