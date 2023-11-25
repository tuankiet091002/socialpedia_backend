package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUser;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.Resource;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserPermission;
import com.java.java_proj.entities.miscs.CustomUserDetail;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.UserPermissionRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.ResourceService;
import com.java.java_proj.services.templates.UserService;
import com.java.java_proj.util.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserPermissionRepository userPermissionRepository;
    @Autowired
    ResourceService resourceService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    DateFormatter dateFormatter;

    @Override
    public User getOwner() {
        try {
            return ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        } catch (Exception e) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Current user not found.");
        }

    }

    @Override
    public Page<LResponseUser> getAllUser(Integer id, String name, String email,
                                          String orderBy, Integer page, Integer size, String orderDirection) {

        // if orderBy = role, need to access field of child class (Permission.role)
        Pageable paging = orderDirection.equals("ASC")
                ? PageRequest.of(page, size, Sort.by(
                Objects.equals(orderBy, "role") ? "role.role" : orderBy).ascending())
                : PageRequest.of(page, size, Sort.by(
                Objects.equals(orderBy, "role") ? "role.role" : orderBy).descending());

        return userRepository.findAllBy(id, name, email, paging);
    }

    @Override
    public DResponseUser createUser(CRequestUser requestUser) {

        // check if email exist
        DResponseUser oldUser = userRepository.findByEmail(requestUser.getEmail());
        if (oldUser != null) {
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
                .createdDate(LocalDateTime.now()).build();

        // find role
        UserPermission role = userPermissionRepository.findByRole(requestUser.getRole());
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
        return userRepository.findByEmail(user.getEmail());
    }


    @Override
    @Transactional
    public DResponseUser updateUser(URequestUser requestUser) {

        // get user from db
        User user = userRepository.findById(requestUser.getId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found"));

        // check if phone exist
        if (userRepository.countByPhone(requestUser.getPhone()) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User with that phone number is existed. Please pick another number.");
        }

        // normal fields set
        user.setName(requestUser.getName());
        user.setPassword(bCryptPasswordEncoder.encode(requestUser.getPassword()));
        user.setPhone(requestUser.getPhone());
        user.setDob(dateFormatter.formatDate(requestUser.getDob()));
        user.setGender(requestUser.getGender());
        user.setIsActive(requestUser.getIsActive());
        user.setModifiedDate(LocalDateTime.now());

        // check role
        UserPermission role = userPermissionRepository.findByRole(requestUser.getRole());
        if (role == null) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Role not found.");
        }
        user.setRole(role);

        // save to db
        userRepository.save(user);
        return userRepository.findByEmail(user.getEmail());
    }

    @Override
    @Transactional
    public DResponseUser updateUserRole(Integer id, String newRole) {

        // check user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "User not found."));

        // check role
        UserPermission role = userPermissionRepository.findByRole(newRole);
        if (role == null) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Role not found.");
        }

        userRepository.updateUserRole(id, role);

        return userRepository.findByEmail(user.getEmail());
    }

    @Override
    public DResponseUser verifyUser(RequestLogin requestLogin) {
        User user = userRepository.findUserByEmail(requestLogin.getEmail());
        if (user == null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        if (!bCryptPasswordEncoder.matches(requestLogin.getPassword(), user.getPassword())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Wrong password.");
        }

        return userRepository.findByEmail(user.getEmail());
    }

    @Override
    public DResponseUser addFriend(Integer userId) {


        User owner = getOwner();
        // check if user exist
        User friend = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found."));

        // check if already friend
        if (owner.getFriends().stream().anyMatch(f -> Objects.equals(f.getId(), friend.getId()))) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is already friend.");
        }

        // add user to both friend list
        owner.getFriends().add(friend);
        userRepository.save(owner);

        friend.getFriends().add(owner);
        userRepository.save(friend);

        // return detail information
        return userRepository.findByEmail(owner.getEmail());
    }

    @Override
    public DResponseUser deleteFriend(Integer userId) {

        User owner = getOwner();
        // check if user exist
        User friend = userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found."));

        // check if already friend
        if (owner.getFriends().stream().noneMatch(f -> Objects.equals(f.getId(), friend.getId()))) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "User is not friend.");
        }

        // add user to both friend list
        owner.setFriends(owner.getFriends().stream()
                .filter(f -> !Objects.equals(f.getId(), friend.getId()))
                .collect(Collectors.toList()));
        userRepository.save(owner);

        // return detail information
        return userRepository.findByEmail(owner.getEmail());
    }
}
