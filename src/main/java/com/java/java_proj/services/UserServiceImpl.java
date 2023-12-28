package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUserPassword;
import com.java.java_proj.dto.request.forupdate.URequestUserRole;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.Resource;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserFriendship;
import com.java.java_proj.entities.UserPermission;
import com.java.java_proj.entities.enums.RequestType;
import com.java.java_proj.entities.miscs.CustomUserDetail;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.UserFriendshipRepository;
import com.java.java_proj.repositories.UserPermissionRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
import com.java.java_proj.util.DateFormatter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    final private BCryptPasswordEncoder bCryptPasswordEncoder;
    final private DateFormatter dateFormatter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserPermissionRepository userPermissionRepository, UserFriendshipRepository userFriendshipRepository, ResourceService resourceService, BCryptPasswordEncoder bCryptPasswordEncoder, DateFormatter dateFormatter) {
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.userFriendshipRepository = userFriendshipRepository;
        this.resourceService = resourceService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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

        return userRepository.findFriendByName(name, user, paging);
    }

    @Override
    public DResponseUser getUserProfile(String userEmail) {

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found."));
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
    public void login(RequestLogin requestLogin) {

        User user = userRepository.findUserByEmail(requestLogin.getEmail());
        if (user == null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        if (!bCryptPasswordEncoder.matches(requestLogin.getPassword(), user.getPassword())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Wrong password.");
        }
    }

    @Override
    public void updateUserProfile(URequestUser requestUser) {

        // get user from db
        User user = userRepository.findById(requestUser.getId())
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
    @Transactional
    public void updateUserRole(URequestUserRole requestUser) {

        // check user
        User user = userRepository.findById(requestUser.getId())
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

        // check role
        UserPermission role = userPermissionRepository.findByName(requestUser.getRole());
        if (role == null) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Role not found.");
        }
        user.setRole(role);

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

        if (user.getAvatar() != null) {
            resourceService.deleteFile(user.getAvatar().getId());
        }

        Resource resource = resourceService.addFile(file);
        user.setAvatar(resource);

        userRepository.save(user);
    }

    @Override
    public void disableUser(Integer userId) {

        // check user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

        // check active status
        if (!user.getIsActive()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is already inactive");
        }
        user.setIsActive(false);

        userRepository.save(user);
    }

    @Override
    public void createFriendRequest(Integer userId) {

        //check user
        User sender = getOwner();
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

        if (friendship.getStatus() != RequestType.PENDING)
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
