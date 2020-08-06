package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.component.CommonException;
import com.thoughtworks.rslist.pgleqi.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static final String INVALID_USER = "invalid user";
    public static final String UNKNOWN_ERROR = "Unknown Error";
    public static List<User> userList = new ArrayList<>();
    ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(RsController.class);

    public UserController() {
        objectMapper = new ObjectMapper();
    }

    @PostMapping("/user")
    public ResponseEntity createOneUser(@RequestBody @Valid User user) {
        if (userList.contains(user)) {
            return generateResponseEntity(user, userList.indexOf(user), HttpStatus.ALREADY_REPORTED);
        }
        userList.add(user);
        return generateResponseEntity(user, userList.size() - 1, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userList;
    }

    private ResponseEntity<User> generateResponseEntity(User user, int index, HttpStatus statusCode) {
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
