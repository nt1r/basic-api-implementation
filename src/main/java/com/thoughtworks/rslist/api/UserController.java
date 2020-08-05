package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.pgleqi.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static List<User> userList;
    ObjectMapper objectMapper;

    public UserController() {
        userList = new ArrayList<>();
        userList.add(new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189"));

        objectMapper = new ObjectMapper();
    }

    @PostMapping("/user")
    public void createOneUser(@RequestBody @Valid User user) {
        userList.add(user);
    }
}
