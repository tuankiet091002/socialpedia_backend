package com.java.java_proj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUserPassword;
import com.java.java_proj.dto.request.forupdate.URequestUserProfile;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.request.security.RequestRefreshToken;
import com.java.java_proj.dto.response.security.ResponseJwt;
import com.java.java_proj.dto.response.security.ResponseRefreshToken;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/auth")
public class AuthController {
    final private UserService userService;
    final private ObjectMapper objectMapper;
    final private Validator validator;

    @Autowired
    public AuthController(UserService userService, ObjectMapper objectMapper, Validator validator) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseJwt> login(@Valid @RequestBody RequestLogin requestLogin) {

        // verified user account
        ResponseJwt response = userService.login(requestLogin);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseRefreshToken> reFreshToken(@Valid @RequestBody RequestRefreshToken requestToken,
                                                             BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        ResponseRefreshToken response = userService.refreshToken(requestToken);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<Null> register(@RequestPart String content,
                                         @RequestPart(required = false) MultipartFile file) throws JsonProcessingException {

        CRequestUser requestUser = objectMapper.readValue(content, CRequestUser.class);

        // get validation error
        DataBinder binder = new DataBinder(requestUser);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }
        // check file
        if (file == null || !file.isEmpty()) {
            requestUser.setAvatarFile(file);
        }
        userService.register(requestUser);


        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<Null> updateUserProfile(@Valid @RequestBody URequestUserProfile requestUser,
                                                  BindingResult bindingResult) {
        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        userService.updateUserProfile(requestUser);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/password")
    public ResponseEntity<Null> updateUserPassword(@RequestBody URequestUserPassword requestUserPassword) {

        userService.updateUserPassword(requestUserPassword);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/avatar")
    public ResponseEntity<Null> updateUserAvatar(@RequestPart MultipartFile file) {

        // get validation error
        if (file.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "File is required.");
        }

        userService.updateUserAvatar(file);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
