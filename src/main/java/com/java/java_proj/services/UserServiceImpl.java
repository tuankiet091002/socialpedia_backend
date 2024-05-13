package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUserPassword;
import com.java.java_proj.dto.request.forupdate.URequestUserProfile;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.request.security.RequestRefreshToken;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.fordetail.DResponseUserFriendship;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.dto.response.security.ResponseJwt;
import com.java.java_proj.dto.response.security.ResponseRefreshToken;
import com.java.java_proj.entities.*;
import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.entities.miscs.CustomUserDetail;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.InboxRepository;
import com.java.java_proj.repositories.UserFriendshipRepository;
import com.java.java_proj.repositories.UserPermissionRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.NotificationService;
import com.java.java_proj.services.templates.RefreshTokenService;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
import com.java.java_proj.util.DateFormatter;
import com.java.java_proj.util.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;
    final private UserPermissionRepository userPermissionRepository;
    final private UserFriendshipRepository userFriendshipRepository;
    final private InboxRepository inboxRepository;
    final private ResourceService resourceService;
    final private JwtTokenProvider tokenProvider;
    final private RefreshTokenService refreshTokenService;
    final private NotificationService notificationService;
    final private AuthenticationManager authenticationManager;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;
    final private ModelMapper modelMapper;
    final private DateFormatter dateFormatter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserPermissionRepository userPermissionRepository, UserFriendshipRepository userFriendshipRepository, InboxRepository inboxRepository, ResourceService resourceService, JwtTokenProvider tokenProvider, RefreshTokenService refreshTokenService, NotificationService notificationService, AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, DateFormatter dateFormatter) {
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.userFriendshipRepository = userFriendshipRepository;
        this.inboxRepository = inboxRepository;
        this.resourceService = resourceService;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.notificationService = notificationService;
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

        return userRepository.findByNameContaining(name, paging).map(user -> modelMapper.map(user, LResponseUser.class));
    }

    @Override
    public Page<LResponseUser> getFriendList(String name, Integer page, Integer size) {
        // get owner
        User owner = getOwner();

        // if orderBy = role, need to access field of child class (Permission.role)
        Pageable paging = PageRequest.of(page, size);

        return userRepository.findFriendsByName(name, owner, paging).map(user -> modelMapper.map(user, LResponseUser.class));
    }

    @Override
    @Transactional
    public DResponseUser getUserProfile(Integer userId) {

        // get raw entity
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found."));

        return modelMapper.map(user, DResponseUser.class);
    }

    @Override
    public DResponseUserFriendship getUserFriendship(Integer userId) {

        User sender = getOwner();
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

        // get friendship
        Optional<UserFriendship> optionalUserFriendship = userFriendshipRepository.findByUser(sender, receiver);
        if (optionalUserFriendship.isPresent()) {
            // map to dto
            UserFriendship userFriendship = optionalUserFriendship.get();
            DResponseUserFriendship response = modelMapper.map(userFriendship, DResponseUserFriendship.class);

            // set other fields
            response.setSenderId(userFriendship.getSender().getId());
            response.setReceiverId(userFriendship.getReceiver().getId());
            // get inbox
            Inbox inbox = inboxRepository.findByFriendshipAndIsActive(userFriendship, true).orElse(null);
            response.setInboxId(inbox != null ? inbox.getId() : null);

            return response;
        }
        return null;
        // find friendship then map
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

        // find user role
        UserPermission role = userPermissionRepository.findByName("user");
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
        User user = userRepository.findByEmail(requestLogin.getEmail())
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

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
        response.setUser(getUserProfile(user.getId()));

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
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

        // create request
        Optional<UserFriendship> optionalUserFriendship = userFriendshipRepository.findByUser(sender, receiver);
        if (optionalUserFriendship.isPresent()) {
            // check old status
            if (optionalUserFriendship.get().getStatus() != RequestType.REJECTED)
                throw new HttpException(HttpStatus.BAD_REQUEST, "Invalid request.");

            // then delete
            userFriendshipRepository.delete(optionalUserFriendship.get());
        }

        // save new entity
        userFriendshipRepository.save(UserFriendship.builder()
                .sender(sender)
                .receiver(receiver)
                .status(RequestType.PENDING)
                .modifiedDate(LocalDateTime.now())
                .build());

        // create notification and send socket message
        notificationService.friendRequestSend(sender, receiver);
    }

    @Override
    @Transactional
    public void acceptFriendRequest(Integer userId) {

        UserFriendship friendship = findFriendship(userId);

        // change request status
        if (friendship.getStatus() != RequestType.PENDING)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Friend request is not pending");
        if (!Objects.equals(friendship.getSender().getId(), userId))
            throw new HttpException(HttpStatus.BAD_REQUEST, "Can't accept your own request");
        friendship.setStatus(RequestType.ACCEPTED);

        userFriendshipRepository.save(friendship);

        // create notification and send socket message
        notificationService.friendRequestAccepted(friendship.getReceiver(), friendship.getSender());
    }

    @Override
    @Transactional
    public void rejectFriendRequest(Integer userId) {

        UserFriendship friendship = findFriendship(userId);

        // change request status
        if (friendship.getStatus() != RequestType.PENDING)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Friend request is not pending");
        if (!Objects.equals(friendship.getSender().getId(), userId))
            throw new HttpException(HttpStatus.BAD_REQUEST, "Can't reject your own request");

        friendship.setStatus(RequestType.REJECTED);

        userFriendshipRepository.save(friendship);

        // seen all friend request message
        notificationService.seenByUserAndDestination(friendship.getReceiver(), "/user/" + userId);
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
