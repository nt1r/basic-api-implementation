package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.pgleqi.RsEvent;
import com.thoughtworks.rslist.pgleqi.User;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.thoughtworks.rslist.util.Convertor.convertRsEvent2RsEventEntity;
import static com.thoughtworks.rslist.util.Convertor.convertUser2UserEntity;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    public static final String RS_EVENT_NOT_EXIST = "rs event not exist";
    public static final String INVALID_REQUEST_PARAM = "invalid request param";
    public static final String INVALID_PARAM = "invalid param";
    final String GET_ONE_RS_EVENT_BY_INDEX_URL = "/rs/%d";
    final String GET_MULTIPLE_RS_EVENT_URL = "/rs/list?start=%d&end=%d";
    final String POST_ONE_RS_EVENT_URL = "/rs";
    final String PUT_ONE_RS_EVENT_URL = "/rs/?index=%d";
    final String DELETE_ONE_RS_EVENT_URL = "/rs/?index=%d";

    final User userDwight = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189");
    final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.save(convertUser2UserEntity(userDwight));

        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第一条事件", "分类一", userDwight)));
        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第二条事件", "分类二", userDwight)));
        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第三条事件", "分类三", userDwight)));
    }

    @Test
    public void should_receive_string_list_with_200_ok() throws Exception {
        mockMvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_get_one_rs_event_by_index() throws Exception {
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_BY_INDEX_URL, 0)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyword", is("分类一")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_BY_INDEX_URL, 1)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("分类二")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_BY_INDEX_URL, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("分类三")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_get_multiple_rs_event_by_indexes() throws Exception {
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 0, 1)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 1, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类二")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类三")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 0, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_add_one_rs_event_by_json() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"eventName\":\"第四条事件\",\"keyword\":\"分类四\",\"user\":{\"user_name\":\"Dwight\",\"user_age\":25,\"user_gender\":\"male\",\"user_email\":\"michaelleqihust@gmail.com\",\"user_phone\":\"18706789189\"}}"))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_BY_INDEX_URL, 3)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第四条事件")))
                .andExpect(jsonPath("$.keyword", is("分类四")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);

        assertTrue(mvcResult.getResponse().containsHeader("index"));
        assertEquals("3", mvcResult.getResponse().getHeader("index"));
    }

    @Test
    public void should_update_third_rs_event() throws Exception {
        mockMvc.perform(put(String.format(PUT_ONE_RS_EVENT_URL, 2))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"eventName\":\"事件已更改\",\"keyword\":\"分类已更改\",\"user\":{\"user_name\":\"Dwight\",\"user_age\":25,\"user_gender\":\"male\",\"user_email\":\"michaelleqihust@gmail.com\",\"user_phone\":\"18706789189\"}}"))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_BY_INDEX_URL, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("事件已更改")))
                .andExpect(jsonPath("$.keyword", is("分类已更改")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_delete_third_rs_event() throws Exception {
        mockMvc.perform(delete(String.format(DELETE_ONE_RS_EVENT_URL, 2)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_throw_bad_request_if_event_name_is_null_when_add_new_rs_event() throws Exception {
        RsEvent nameNullEvent = new RsEvent(null, "分类四", userDwight);

        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(nameNullEvent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_bad_request_if_keyword_is_null_when_add_new_rs_event() throws Exception {
        RsEvent keywordNullEvent = new RsEvent("第四条事件", null, userDwight);

        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(keywordNullEvent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_bad_request_if_user_is_null_when_add_new_rs_event() throws Exception {
        RsEvent userNullEvent = new RsEvent("第四条事件", "分类四", null);

        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(userNullEvent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_bad_request_when_index_invalid() throws Exception {
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_BY_INDEX_URL, 8)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(RS_EVENT_NOT_EXIST)));
    }

    @Test
    public void should_return_bad_request_when_range_of_index_invalid() throws Exception {
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 5, 7)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(INVALID_REQUEST_PARAM)));

        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 2, 0)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(INVALID_REQUEST_PARAM)));
    }

    @Test
    public void should_return_bad_request_when_rs_event_param_invalid() throws Exception {
        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"eventName\":null,\"keyword\":\"分类四\",\"user\":{\"user_name\":\"Dwight\",\"user_age\":25,\"user_gender\":\"male\",\"user_email\":\"michaelleqihust@gmail.com\",\"user_phone\":\"18706789189\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(INVALID_PARAM)));
    }

    @Test
    public void should_return_bad_request_when_user_param_invalid() throws Exception {
        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"eventName\":\"第四条事件\",\"keyword\":\"分类四\",\"user\":{\"user_name\":\"Dwight\",\"user_age\":200,\"user_gender\":\"male\",\"user_email\":\"michaelleqihust@gmail.com\",\"user_phone\":\"18706789189\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(INVALID_PARAM)));
    }
}