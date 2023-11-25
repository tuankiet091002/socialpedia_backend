package com.java.java_proj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.java_proj.dto.request.forcreate.CRequestUser;
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
import com.java.java_proj.util.JWTTokenProvider;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/auth")
@Api(tags = "Auth")
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    JWTTokenProvider tokenProvider;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Validator validator;

    @PostMapping("/login")
    public ResponseEntity<ResponseJwt> login(@Valid @RequestBody RequestLogin requestLogin) {
        // verified user account
        DResponseUser loginUser = userService.verifyUser(requestLogin);

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        refreshTokenService.deActiveUserToken(loginUser.getId());

        ResponseJwt response = new ResponseJwt();
        response.setToken(tokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal()));
        response.setRefreshToken(refreshTokenService.createToken(loginUser.getEmail()).getToken());
        response.setUser(loginUser);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/refresh-token")
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
    public ResponseEntity<DResponseUser> register(@RequestPart String content,
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


        DResponseUser user = userService.createUser(requestUser);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
