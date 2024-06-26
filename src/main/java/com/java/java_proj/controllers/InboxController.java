package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forupdate.URequestInbox;
import com.java.java_proj.dto.response.fordetail.DResponseInbox;
import com.java.java_proj.dto.response.forlist.LResponseInbox;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.InboxService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/inbox")
public class InboxController {

    final private InboxService inboxService;

    @Autowired
    public InboxController(InboxService inboxService) {
        this.inboxService = inboxService;
    }

    @GetMapping("")
    public ResponseEntity<Page<LResponseInbox>>
    getInboxList(@RequestParam(value = "name", defaultValue = "") String name,
                 @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                 @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {

        Page<LResponseInbox> inboxPage = inboxService.getInboxList(name, page, size);

        return new ResponseEntity<>(inboxPage, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{inboxId}")
    public ResponseEntity<DResponseInbox> getInboxProfile(@PathVariable Integer inboxId) {

        DResponseInbox inbox = inboxService.getInboxProfile(inboxId);

        return new ResponseEntity<>(inbox, HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Null> createInbox(@PathVariable Integer userId) {

        inboxService.createInbox(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{userId}/profile")
//    @PreAuthorize("hasPermission(#userId, 'USER', 'MODIFIY')")
    public ResponseEntity<Null> updateInboxProfile(@PathVariable Integer userId,
                                                   @Valid @RequestBody URequestInbox requestChannel,
                                                   BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        inboxService.updateInboxProfile(userId, requestChannel);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
