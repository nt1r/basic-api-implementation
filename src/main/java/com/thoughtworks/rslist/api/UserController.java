package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.component.CommonException;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.pgleqi.User;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.util.Convertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.thoughtworks.rslist.util.Convertor.convertUser2UserEntity;
import static com.thoughtworks.rslist.util.Convertor.convertUserEntity2User;
import static com.thoughtworks.rslist.util.Generator.generateResponseEntity;

@RestController
public class UserController {
    public static final String INVALID_USER = "invalid user";
    public static final String UNKNOWN_ERROR = "Unknown Error";
    public static final String USER_ID_NOT_EXIST = "userId not exist";
    // public static List<User> userList = new ArrayList<>();
    @Autowired
    public UserRepository userRepository;
    ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(RsController.class);

    public UserController() {
        objectMapper = new ObjectMapper();
    }

    @PostMapping("/user")
    public ResponseEntity createOneUser(@RequestBody @Valid User user) {
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            UserEntity userEntity = userRepository.findByUserName(user.getUserName()).get();
            int userIndex = userRepository.findAll().indexOf(userEntity);
            return generateResponseEntity(user, userIndex, HttpStatus.ALREADY_REPORTED);
        }
        userRepository.save(convertUser2UserEntity(user));
        return generateResponseEntity(user, userRepository.count() - 1, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<UserEntity> userEntityList = userRepository.findAll();
        return convertUserEntity2User(userEntityList);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getOneUser(@PathVariable int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException(USER_ID_NOT_EXIST);
        }
        UserEntity userEntity = userRepository.findById(userId).get();
        return ResponseEntity.ok(convertUserEntity2User(userEntity));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity deleteOneUser(@PathVariable int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException(USER_ID_NOT_EXIST);
        }
        userRepository.deleteById(userId);
        return ResponseEntity.ok(userId);
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
