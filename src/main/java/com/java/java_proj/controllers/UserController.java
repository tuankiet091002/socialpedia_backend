package com.java.java_proj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUser;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.request.security.RequestRefreshToken;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.dto.response.security.ResponseJwt;
import com.java.java_proj.dto.response.security.ResponseRefreshToken;
import com.java.java_proj.entities.RefreshToken;
import com.java.java_proj.entities.miscs.CustomUserDetail;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.RefreshTokenService;
import com.java.java_proj.services.templates.UserService;
import com.java.java_proj.util.JWTTokenProvider;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "User")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<Page<LResponseUser>> getAllUser(@RequestParam(value = "id", defaultValue = "0") Integer id,
                                                          @RequestParam(value = "name", defaultValue = "") String name,
                                                          @RequestParam(value = "email", defaultValue = "") String email,
                                                          @RequestParam(value = "orderBy", defaultValue = "dob") String orderBy,
                                                          @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                          @RequestParam(value = "orderDirection", defaultValue = "DESC") String orderDirection) {

        List<String> allowedFields = Arrays.asList("id", "name", "email", "dob", "gender", "role");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        List<String> allowedSort = Arrays.asList("ASC", "DESC");
        if (!allowedSort.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Sort Direction " + orderDirection + " is illegal!");
        }

        Page<LResponseUser> userPage = userService.getAllUser(id, name, email, orderBy, page, size, orderDirection);

        return new ResponseEntity<>(userPage, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<DResponseUser> updateUser(@Valid @RequestBody URequestUser requestUser,
                                                    BindingResult bindingResult) {
        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseUser user = userService.updateUser(requestUser);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/role")
    public ResponseEntity<DResponseUser> updateUserRole(@PathVariable Integer id, @RequestParam("role") String role) {

        if (!role.equals("trainer") && !role.equals("class_admin") && !role.equals("super_admin")) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Role is invalid");
        }

        DResponseUser user = userService.updateUserRole(id, role);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}/friend")
    public ResponseEntity<DResponseUser> addFriend(@PathVariable Integer userId) {

        DResponseUser user = userService.addFriend(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}/friend")
    public ResponseEntity<DResponseUser> deleteFriend(@PathVariable Integer userId) {

        DResponseUser user = userService.deleteFriend(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
