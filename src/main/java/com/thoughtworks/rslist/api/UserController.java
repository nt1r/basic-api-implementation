package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.component.CommonException;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.pgleqi.User;
import com.thoughtworks.rslist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static final String INVALID_USER = "invalid user";
    public static final String UNKNOWN_ERROR = "Unknown Error";
    // public static List<User> userList = new ArrayList<>();
    public static UserRepository userRepository;
    ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(RsController.class);

    public UserController(UserRepository userRepository) {
        objectMapper = new ObjectMapper();
        this.userRepository = userRepository;
    }

    public static Integer findUserIndex(User user) {
        List<UserEntity> allUsers = userRepository.findAll();
        int index = 0;
        for (UserEntity userEntity : allUsers) {
            if (userEntity.getUserName().equals(user.getUserName())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static List<User> convertUserEntity2User(List<UserEntity> userEntityList) {
        List<User> convertedResultList = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            convertedResultList.add(convertUserEntity2User(userEntity));
        }
        return convertedResultList;
    }

    public static User convertUserEntity2User(UserEntity userEntity) {
        return new User(userEntity.getUserName(),
                userEntity.getAge(),
                userEntity.getGender(),
                userEntity.getEmail(),
                userEntity.getPhone());
    }

    public static UserEntity convertUser2UserEntity(User user) {
        return UserEntity.builder()
                .userName(user.getUserName())
                .age(user.getAge())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .votes(10)
                .build();
    }

    @PostMapping("/user")
    public ResponseEntity createOneUser(@RequestBody @Valid User user) {
        int index = findUserIndex(user);
        if (index > -1) {
            return generateResponseEntity(user, index, HttpStatus.ALREADY_REPORTED);
        }

        userRepository.save(convertUser2UserEntity(user));
        return generateResponseEntity(user, userRepository.count() - 1, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<UserEntity> userEntityList = userRepository.findAll();
        return convertUserEntity2User(userEntityList);
    }

    private ResponseEntity<User> generateResponseEntity(User user, long index, HttpStatus statusCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("index", String.valueOf(index));
        return new ResponseEntity<>(user, httpHeaders, statusCode);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommonException> handleCommonExceptions(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException) {
            logger.error(INVALID_USER);
            return ResponseEntity.badRequest().body(new CommonException(INVALID_USER));
        } else {
            logger.error(INVALID_USER);
            throw new RuntimeException(UNKNOWN_ERROR);
        }
    }
}
