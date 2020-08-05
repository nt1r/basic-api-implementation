package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.pgleqi.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
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
            return new ResponseEntity<>(user, HttpStatus.ALREADY_REPORTED);
        }
        userList.add(user);
        return ResponseEntity.ok(user);
    }
}
