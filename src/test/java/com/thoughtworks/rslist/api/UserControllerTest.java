package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.pgleqi.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    final String ADD_USER_URL = "/user";
    final String GET_ALL_USERS = "/users";

    final User userDwight = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189");
    final User userNameTooLong = new User("DwightClaudette", 25, "male", "michaelleqihust@gmail.com", "18706789189");
    final User userNameNull = new User(null, 25, "male", "michaelleqihust@gmail.com", "18706789189");
    final User userGenderNull = new User("Dwight", 25, null, "michaelleqihust@gmail.com", "18706789189");
    final User userOver100Ages = new User("Dwight", 250, "male", "michaelleqihust@gmail.com", "18706789189");
    final User userBelow18Ages = new User("Dwight", 15, "male", "michaelleqihust@gmail.com", "18706789189");
    final User userAgeNull = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189");
    final User userEmailInvalid = new User("Dwight", 25, "male", "aaa%@@gmail.com", "18706789189");
    final User userPhoneNumberNotStartWithOne = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "88706789189");
    final User userPhoneNumberNotContain11Numbers = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "187067891");
    final User userPhoneNumberNull = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", null);
    final User userClaudette = new User("Claudette", 23, "female", "michaelleqisnnu@gmail.com", "18888888888");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
        UserController.userList.clear();
    }

    @Test
    void should_create_one_user() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userDwight)))
                .andExpect(status().isCreated())
                .andReturn();
        assertEquals(1, UserController.userList.size());

        assertTrue(mvcResult.getResponse().containsHeader("index"));
        assertEquals("0", mvcResult.getResponse().getHeader("index"));
    }

    @Test
    void should_not_create_exist_user() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userDwight)))
                .andExpect(status().isCreated());
        int userCountBeforeCreateSameUser = UserController.userList.size();

        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userDwight)))
                .andExpect(status().isAlreadyReported());
        int userCountAfterCreateSameUser = UserController.userList.size();

        assertEquals(userCountBeforeCreateSameUser, userCountAfterCreateSameUser);
    }

    @Test
    void should_not_create_user_with_name_over_8_characters() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userNameTooLong)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_user_with_name_null() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userNameNull)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_user_with_gender_null() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userGenderNull)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_user_with_over_100_age() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userOver100Ages)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_user_with_below_18_age() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userBelow18Ages)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_user_with_age_null() throws Exception {
        userAgeNull.setAge(null);
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userAgeNull)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_user_with_invalid_email() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userEmailInvalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_user_with_phone_number_not_start_with_1() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userPhoneNumberNotStartWithOne)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_user_with_phone_number_not_contains_11_numbers() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userPhoneNumberNotContain11Numbers)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_user_with_phone_number_null() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userPhoneNumberNull)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_rename_user_properties_in_response_body() throws Exception {
        UserController.userList.add(userDwight);
        UserController.userList.add(userClaudette);

        mockMvc.perform(get(GET_ALL_USERS))
                .andExpect(jsonPath("$[0]", hasKey("user_name")))
                .andExpect(jsonPath("$[0].user_name", is("Dwight")))
                .andExpect(jsonPath("$[0]", hasKey("user_age")))
                .andExpect(jsonPath("$[0]", hasKey("user_gender")))
                .andExpect(jsonPath("$[0]", hasKey("user_email")))
                .andExpect(jsonPath("$[0]", hasKey("user_phone")))
                .andExpect(jsonPath("$[1]", hasKey("user_name")))
                .andExpect(jsonPath("$[1].user_name", is("Claudette")))
                .andExpect(jsonPath("$[1]", hasKey("user_age")))
                .andExpect(jsonPath("$[1]", hasKey("user_gender")))
                .andExpect(jsonPath("$[1]", hasKey("user_email")))
                .andExpect(jsonPath("$[1]", hasKey("user_phone")))
                .andExpect(status().isOk());
    }
}