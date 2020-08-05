package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.pgleqi.User;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static List<User> userList = new ArrayList<>();
    ObjectMapper objectMapper;

    public UserController() {
        objectMapper = new ObjectMapper();
    }

    @PostMapping("/user")
    public ResponseEntity<User> createOneUser(@RequestBody @Valid User user) {
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
}
