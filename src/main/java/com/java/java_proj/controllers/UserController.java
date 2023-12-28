package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forupdate.URequestUserRole;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    final private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'VIEW')")
    public ResponseEntity<Page<LResponseUser>> getUserList(@RequestParam(value = "name", defaultValue = "") String name,
                                                           @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                           @RequestParam(value = "orderBy", defaultValue = "dob") String orderBy,
                                                           @RequestParam(value = "orderDirection", defaultValue = "DESC") String orderDirection) {

        List<String> allowedFields = Arrays.asList("id", "name", "email", "dob", "gender", "role");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        List<String> allowedSort = Arrays.asList("ASC", "DESC");
        if (!allowedSort.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Sort Direction " + orderDirection + " is illegal!");
        }

        Page<LResponseUser> userPage = userService.getUserList(name, page, size, orderBy, orderDirection);

        return new ResponseEntity<>(userPage, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/friend")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Page<LResponseUser>> getFriendList(@RequestParam(value = "name", defaultValue = "") String name,
                                                             @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                             @RequestParam(value = "orderBy", defaultValue = "dob") String orderBy,
                                                             @RequestParam(value = "orderDirection", defaultValue = "DESC") String orderDirection) {

        List<String> allowedFields = Arrays.asList("id", "name", "email", "dob", "gender", "role");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        List<String> allowedSort = Arrays.asList("ASC", "DESC");
        if (!allowedSort.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Sort Direction " + orderDirection + " is illegal!");
        }

        Page<LResponseUser> userPage = userService.getFriendList(name, page, size, orderBy, orderDirection);

        return new ResponseEntity<>(userPage, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{userEmail}")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<DResponseUser> getUserProfile(@PathVariable String userEmail) {

        DResponseUser user = userService.getUserProfile(userEmail);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/role")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'MODIFY')")
    public ResponseEntity<Null> updateUserRole(@Valid @RequestBody URequestUserRole requestUser,
                                               BindingResult bindingResult) {
        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        userService.updateUserRole(requestUser);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'MODIFY')")
    public ResponseEntity<Null> disableUser(@PathVariable Integer userId) {

        userService.disableUser(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/{userId}/friend")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Null> createFriendRequest(@PathVariable Integer userId) {

        userService.createFriendRequest(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{userId}/friend/accept")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Null> acceptFriendRequest(@PathVariable Integer userId) {

        userService.acceptFriendRequest(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{userId}/friend/reject")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Null> rejectFriendRequest(@PathVariable Integer userId) {

        userService.rejectFriendRequest(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/friend")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Null> unFriend(@PathVariable Integer userId) {

        userService.unFriend(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
