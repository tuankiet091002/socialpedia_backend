package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUserPassword;
import com.java.java_proj.dto.request.forupdate.URequestUserProfile;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.request.security.RequestRefreshToken;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.fordetail.DResponseUserPermission;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.dto.response.security.ResponseJwt;
import com.java.java_proj.dto.response.security.ResponseRefreshToken;
import com.java.java_proj.entities.*;
import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.entities.miscs.CustomUserDetail;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.UserFriendshipRepository;
import com.java.java_proj.repositories.UserPermissionRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.RefreshTokenService;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
import com.java.java_proj.util.DateFormatter;
import com.java.java_proj.util.security.JWTTokenProvider;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;
    final private UserPermissionRepository userPermissionRepository;
    final private UserFriendshipRepository userFriendshipRepository;
    final private ResourceService resourceService;
    final private JWTTokenProvider tokenProvider;
    final private RefreshTokenService refreshTokenService;
    final private AuthenticationManager authenticationManager;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;

    final private ModelMapper modelMapper;
    final private DateFormatter dateFormatter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserPermissionRepository userPermissionRepository, UserFriendshipRepository userFriendshipRepository, ResourceService resourceService, JWTTokenProvider tokenProvider, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, DateFormatter dateFormatter) {
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.userFriendshipRepository = userFriendshipRepository;
        this.resourceService = resourceService;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.dateFormatter = dateFormatter;
    }

    @Override
    public User getOwner() {
        try {
            return ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        } catch (Exception e) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Current user not found.");
        }

    }

    @Override
    public Page<LResponseUser> getUserList(String name, Integer page, Integer size, String orderBy, String orderDirection) {

        // if orderBy = role, need to access field of child class (Permission.role)
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(page, size, Sort.by(
                Objects.equals(orderBy, "role") ? "role.name" : orderBy).ascending())
                : PageRequest.of(page, size, Sort.by(
                Objects.equals(orderBy, "role") ? "role.name" : orderBy).descending());

        return userRepository.findByNameContaining(name, paging);
    }

    @Override
    public Page<LResponseUser> getFriendList(String name, Integer page, Integer size, String orderBy, String orderDirection) {
        // get owner
        User user = getOwner();

        // if orderBy = role, need to access field of child class (Permission.role)
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(page, size, Sort.by(
                Objects.equals(orderBy, "role") ? "role.name" : orderBy).ascending())
                : PageRequest.of(page, size, Sort.by(
                Objects.equals(orderBy, "role") ? "role.name" : orderBy).descending());

        return userRepository.findFriendsByName(name, user, paging);
    }

    @Override
    @Transactional
    public DResponseUser getUserProfile(String userEmail) {

        // get raw entity
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found."));

        // map to dto
        DResponseUser responseUser = modelMapper.map(user, DResponseUser.class);
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        responseUser.setRole(pf.createProjection(DResponseUserPermission.class, user.getRole()));
        responseUser.setFriends(userRepository.findFriends(user));

        return responseUser;
    }

    @Override
    public void register(CRequestUser requestUser) {

        // check if email exist
        if (userRepository.countByEmail(requestUser.getEmail()) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Email address is existed. Please check and input another email address.");
        }

        // check if phone exist
        if (userRepository.countByPhone(requestUser.getPhone()) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User with that phone number is existed. Please pick another number.");
        }

        // normal fields set
        User user = User.builder()
                .name(requestUser.getName())
                .email(requestUser.getEmail())
                .password(bCryptPasswordEncoder.encode(requestUser.getPassword()))
                .phone(requestUser.getPhone())
                .gender(requestUser.getGender())
                .dob(dateFormatter.formatDate((requestUser.getDob())))
                .isActive(true)
                .modifiedDate(LocalDateTime.now())
                .build();

        // find role
        UserPermission role = userPermissionRepository.findByName(requestUser.getRole());
        if (role == null) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Role not found.");
        }
        user.setRole(role);

        // check avatar file
        if (requestUser.getAvatarFile() != null) {
            Resource resource = resourceService.addFile(requestUser.getAvatarFile());
            user.setAvatar(resource);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public ResponseJwt login(RequestLogin requestLogin) {

        // find user
        User user = userRepository.findUserByEmail(requestLogin.getEmail());
        if (user == null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        // check password
        if (!bCryptPasswordEncoder.matches(requestLogin.getPassword(), user.getPassword())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Wrong password.");
        }

        // register authentication
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // de-active old token
        refreshTokenService.deActiveUserToken(user.getId());

        // return result
        ResponseJwt response = new ResponseJwt();
        response.setToken(tokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal()));
        response.setRefreshToken(refreshTokenService.createToken(user.getEmail()).getToken());
        response.setUser(getUserProfile(requestLogin.getEmail()));

        return response;
    }

    @Override
    public ResponseRefreshToken refreshToken(RequestRefreshToken requestToken) {

        // find unexpired token
        RefreshToken refreshToken = refreshTokenService.findActiveToken(requestToken.getRefreshToken());
        if (refreshToken == null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Refresh token is invalid");
        }

        CustomUserDetail userDetail;
        // get user detail
        try {
            userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Failed to fetch current user");
        }

        ResponseRefreshToken response = new ResponseRefreshToken();
        response.setAccessToken(tokenProvider.generateToken(userDetail));

        return response;
    }

    @Override
    public void updateUserProfile(URequestUserProfile requestUser) {

        // get user from db
        User user = userRepository.findById(getOwner().getId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found"));

        // check if phone exist
        if (!Objects.equals(requestUser.getPhone(), user.getPhone()) && userRepository.countByPhone(requestUser.getPhone()) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User with that phone number is existed. Please pick another number.");
        }

        // normal fields set
        user.setName(requestUser.getName());
        user.setPhone(requestUser.getPhone());
        user.setDob(dateFormatter.formatDate(requestUser.getDob()));
        user.setGender(requestUser.getGender());
        user.setModifiedDate(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public void updateUserRole(Integer userId, String role) {

        // check user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

        // check role
        UserPermission newRole = userPermissionRepository.findByName(role);
        if (role == null) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Role not found.");
        }
        user.setRole(newRole);

        userRepository.save(user);
    }

    @Override
    public void updateUserPassword(URequestUserPassword requestUserPassword) {

        // check user
        User user = getOwner();

        // compare two password
        if (!bCryptPasswordEncoder.matches(requestUserPassword.getOldPassword(), user.getPassword())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Wrong password.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(requestUserPassword.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    public void updateUserAvatar(MultipartFile file) {

        // get user from session
        User user = getOwner();

        // mark old avatar to delete
        Integer oldResource = null;
        if (user.getAvatar() != null) {
            oldResource = user.getAvatar().getId();
        }

        // set and save
        Resource resource = resourceService.addFile(file);
        user.setAvatar(resource);
        userRepository.save(user);

        if (oldResource != null)
            resourceService.deleteFile(oldResource);
    }

    @Override
    public void disableUser(Integer userId) {

        // check user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

        user.setIsActive(false);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createFriendRequest(Integer userId) {

        // need to be persisted
        User sender = userRepository.findById(getOwner().getId())
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));
        ;
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

        // create request
        UserFriendship friendship = userFriendshipRepository.findByUser(sender, receiver)
                .orElse(UserFriendship.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .status(RequestType.PENDING)
                        .modifiedDate(LocalDateTime.now())
                        .build());

        userFriendshipRepository.save(friendship);
    }

    @Override
    @Transactional
    public void acceptFriendRequest(Integer userId) {

        UserFriendship friendship = findFriendship(userId);

        // change request status
        if (friendship.getStatus() != RequestType.PENDING)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Friend request is not pending");
        friendship.setStatus(RequestType.ACCEPTED);

        userFriendshipRepository.save(friendship);
    }

    @Override
    @Transactional
    public void rejectFriendRequest(Integer userId) {

        UserFriendship friendship = findFriendship(userId);

        // change request status
        if (friendship.getStatus() != RequestType.PENDING)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Friend request is not pending");
        friendship.setStatus(RequestType.REJECTED);

        userFriendshipRepository.save(friendship);

    }

    @Override
    @Transactional
    public void unFriend(Integer userId) {

        // change request status
        UserFriendship friendship = findFriendship(userId);

        if (friendship.getStatus() != RequestType.ACCEPTED)
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is not your friend.");
        friendship.setStatus(RequestType.REJECTED);

        userFriendshipRepository.save(friendship);
    }

    @Override
    public UserFriendship findFriendship(Integer userId) {

        //check user
        User sender = getOwner();
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

        // change request status
        return userFriendshipRepository.findByUser(sender, receiver)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Friend request not found"));
    }
}
