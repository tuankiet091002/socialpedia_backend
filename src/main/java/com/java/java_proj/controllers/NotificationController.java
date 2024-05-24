package com.java.java_proj.controllers;

import com.java.java_proj.dto.response.forlist.LResponseNotification;
import com.java.java_proj.services.templates.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    final private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("")
    public ResponseEntity<Page<LResponseNotification>> getNotificationList(@RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {


        Page<LResponseNotification> notificationPage = notificationService.getNotificationList(page, size);

        return new ResponseEntity<>(notificationPage, HttpStatus.OK);
    }
}
