package com.java.java_proj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUserPassword;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.request.security.RequestRefreshToken;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.security.ResponseJwt;
import com.java.java_proj.dto.response.security.ResponseRefreshToken;
import com.java.java_proj.entities.RefreshToken;
import com.java.java_proj.entities.miscs.CustomUserDetail;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.RefreshTokenService;
import com.java.java_proj.services.templates.UserService;
import com.java.java_proj.util.security.JWTTokenProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/auth")
public class AuthController {
    final private UserService userService;
    final private JWTTokenProvider tokenProvider;
    final private RefreshTokenService refreshTokenService;
    final private AuthenticationManager authenticationManager;
    final private ObjectMapper objectMapper;
    final private Validator validator;

    @Autowired
    public AuthController(UserService userService, JWTTokenProvider tokenProvider, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager, ObjectMapper objectMapper, Validator validator) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseJwt> login(@Valid @RequestBody RequestLogin requestLogin) {

        // verified user account
        userService.login(requestLogin);
        DResponseUser user = userService.getUserProfile(requestLogin.getEmail());

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        refreshTokenService.deActiveUserToken(user.getId());

        ResponseJwt response = new ResponseJwt();
        response.setToken(tokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal()));
        response.setRefreshToken(refreshTokenService.createToken(user.getEmail()).getToken());
        response.setUser(user);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseRefreshToken> reFreshToken(@Valid @RequestBody RequestRefreshToken requestRefreshToken,
                                                             BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        RefreshToken refreshToken = refreshTokenService.findActiveToken(requestRefreshToken.getRefreshToken());

        if (refreshToken == null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Refresh token is expired");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Session ID not found, please login again.");
        }

        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        ResponseRefreshToken response = new ResponseRefreshToken();
        response.setAccessToken(tokenProvider.generateToken(userDetail));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<Null> register(@RequestPart String content,
                                         @RequestPart MultipartFile file) throws JsonProcessingException {

        CRequestUser requestUser = objectMapper.readValue(content, CRequestUser.class);

        // get validation error
        DataBinder binder = new DataBinder(requestUser);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }
        if (!file.isEmpty()) {
            requestUser.setAvatarFile(file);
        }
        userService.register(requestUser);


        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Null> changeUserProfile(@Valid @RequestBody URequestUser requestUser,
                                                  BindingResult bindingResult) {
        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        userService.updateUserProfile(requestUser);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/password")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Null> changeUserPassword(@RequestBody URequestUserPassword requestUserPassword) {

        userService.updateUserPassword(requestUserPassword);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/avatar")
    @PreAuthorize("hasPermission('GLOBAL', 'USER', 'SELF')")
    public ResponseEntity<Null> changeUserAvatar(@RequestPart MultipartFile file) {

        userService.updateUserAvatar(file);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
