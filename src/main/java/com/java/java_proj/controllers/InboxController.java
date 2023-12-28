package com.java.java_proj.controllers;

import com.java.java_proj.dto.response.forlist.LResponseChatSpace;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.InboxService;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/inbox")
public class InboxController {

    final private InboxService inboxService;

    @Autowired
    public InboxController(InboxService inboxService) {
        this.inboxService = inboxService;
    }

    @GetMapping("/")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Page<LResponseChatSpace>> getInboxList(@RequestParam(value = "name", defaultValue = "") String name,
                                                                 @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                                 @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
                                                                 @RequestParam(value = "orderDirection", defaultValue = "DESC") String orderDirection) {

        List<String> allowedFields = Arrays.asList("id", "name");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        List<String> allowedSort = Arrays.asList("ASC", "DESC");
        if (!allowedSort.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Sort Direction " + orderDirection + " is illegal!");
        }

        Page<LResponseChatSpace> inboxPage = inboxService.getInboxList(name, page, size, orderBy, orderDirection);

        return new ResponseEntity<>(inboxPage, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Null> createInbox(@PathVariable Integer userId) {

        inboxService.createInbox(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Null> disableInbox(@PathVariable Integer userId) {

        inboxService.disableInbox(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
