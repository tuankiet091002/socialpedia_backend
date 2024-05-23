package com.java.java_proj.controllers;

import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.fordetail.DResponseUserFriendship;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.UserService;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    final private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'VIEW')")
    public ResponseEntity<Page<LResponseUser>> getUserList(@RequestParam(value = "name", defaultValue = "") String name,
                                                           @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "orderBy", defaultValue = "dob") String orderBy,
                                                           @RequestParam(value = "orderDirection", defaultValue = "DESC") String orderDirection) {

        List<String> allowedFields = Arrays.asList("id", "name", "email", "phone", "dob", "gender", "role");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        List<String> allowedSort = Arrays.asList("ASC", "DESC");
        if (!allowedSort.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Sort Direction " + orderDirection + " is illegal!");
        }

        Page<LResponseUser> userPage = userService.getUserList(name, pageNo, pageSize, orderBy, orderDirection);

        return new ResponseEntity<>(userPage, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/friend")
    public ResponseEntity<Page<LResponseUser>> getFriendList(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {

        Page<LResponseUser> userPage = userService.getFriendList(name, page, size);

        return new ResponseEntity<>(userPage, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DResponseUser> getUserProfile(@PathVariable Integer userId) {

        DResponseUser user = userService.getUserProfile(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}/friendship")
    public ResponseEntity<DResponseUserFriendship> getUserFriendship(@PathVariable Integer userId) {

        DResponseUserFriendship friendship = userService.getUserFriendship(userId);

        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'MODIFY')")
    @CacheEvict(value = "userCache", key = "#userId")
    public ResponseEntity<Null> updateUserRole(@PathVariable Integer userId,
                                               @RequestParam(value = "role") String role) {

        userService.updateUserRole(userId, role);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'MODIFY')")
    @Caching(evict = {
            @CacheEvict(value = "userCache", key = "#userId"),
            @CacheEvict(value = "userPageCache", allEntries = true)
    })
    public ResponseEntity<Null> disableUser(@PathVariable Integer userId) {

        userService.disableUser(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/{userId}/friend")
    public ResponseEntity<Null> createFriendRequest(@PathVariable Integer userId) {

        userService.createFriendRequest(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{userId}/friend/accept")
    public ResponseEntity<Null> acceptFriendRequest(@PathVariable Integer userId) {

        userService.acceptFriendRequest(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{userId}/friend/reject")
    public ResponseEntity<Null> rejectFriendRequest(@PathVariable Integer userId) {

        userService.rejectFriendRequest(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/friend")
    public ResponseEntity<Null> unFriend(@PathVariable Integer userId) {

        userService.unFriend(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
