package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.pgleqi.User;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    final String ADD_USER_URL = "/user";
    final String GET_ALL_USERS_URL = "/users";
    final String GET_ONE_USER_URL = "/user/%d";
    final String DELETE_ONE_USER_URL = "/user/%d";

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
    final User userWithGenderCat = new User("Dwight", 25, "cat", "michaelleqihust@gmail.com", "18706789189");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
        // UserController.userList.clear();
        UserController.userRepository = userRepository;
        UserController.userRepository.deleteAll();
    }

    @Test
    void should_create_one_user() throws Exception {
        MvcResult mvcResult = addOneNormalUserTest();

        assertEquals(1L, UserController.userRepository.count());
        assertTrue(mvcResult.getResponse().containsHeader("index"));
        assertEquals("0", mvcResult.getResponse().getHeader("index"));
    }

    @Test
    void should_not_create_exist_user() throws Exception {
        addOneNormalUserTest();
        long userCountBeforeCreateSameUser = UserController.userRepository.count();

        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userDwight)))
                .andExpect(status().isAlreadyReported());
        long userCountAfterCreateSameUser = UserController.userRepository.count();

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
        UserController.userRepository.save(UserController.convertUser2UserEntity(userDwight));
        UserController.userRepository.save(UserController.convertUser2UserEntity(userClaudette));

        mockMvc.perform(get(GET_ALL_USERS_URL))
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

    @Test
    void should_return_bad_request_if_post_params_invalid_when_create_one_user() throws Exception {
        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userDwight)))
                .andExpect(status().isCreated());

        mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userWithGenderCat)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(UserController.INVALID_USER)));
    }

    @Test
    void should_return_user_when_get_user_if_id_valid() throws Exception {
        addOneNormalUserTest();

        int userId = UserController.userRepository.findAll().get(0).getID();
        mockMvc.perform(get(String.format(GET_ONE_USER_URL, userId))
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.user_name", is("Dwight")))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_bad_request_when_get_user_if_id_invalid() throws Exception {
        addOneNormalUserTest();

        int userIdInvalid = UserController.userRepository.findAll().get(0).getID() + 1;
        mockMvc.perform(get(String.format(GET_ONE_USER_URL, userIdInvalid))
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.error", is(UserController.USER_ID_NOT_EXIST)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_when_delete_user_exist() throws Exception {
        addOneNormalUserTest();

        int userId = UserController.userRepository.findAll().get(0).getID();
        mockMvc.perform(delete(String.format(DELETE_ONE_USER_URL, userId))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(0L, UserController.userRepository.count());
    }

    @Test
    void should_return_bad_request_when_delete_user_not_exist() throws Exception {
        addOneNormalUserTest();

        int userIdInvalid = UserController.userRepository.findAll().get(0).getID() + 1;
        mockMvc.perform(delete(String.format(DELETE_ONE_USER_URL, userIdInvalid))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(UserController.USER_ID_NOT_EXIST)))
                .andReturn();
        assertEquals(1L, UserController.userRepository.count());
    }

    private MvcResult addOneNormalUserTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(ADD_USER_URL)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userDwight)))
                .andExpect(status().isCreated())
                .andReturn();

        return mvcResult;
    }
}