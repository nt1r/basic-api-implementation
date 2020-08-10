package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
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
    final String GET_ONE_RS_EVENT_BY_INDEX_URL = "/rs/%d";
    final String GET_MULTIPLE_RS_EVENT_URL = "/rs/list?start=%d&end=%d";
    final String POST_ONE_RS_EVENT_URL = "/rs";
    final String PUT_ONE_RS_EVENT_URL = "/rs/?index=%d";
    final String DELETE_ONE_RS_EVENT_URL = "/rs/?index=%d";
    final String PATCH_ONE_RS_EVENT_URL = "/rs/%d";

    final User userDwight = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189");
    int dwightId;
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

        dwightId = userRepository.save(convertUser2UserEntity(userDwight)).getId();

        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第一条事件", "分类一", dwightId, 0)));
        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第二条事件", "分类二", dwightId, 0)));
        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第三条事件", "分类三", dwightId, 0)));
    }

    @Test
    public void should_receive_string_list_with_200_ok() throws Exception {
        mockMvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].voteNum", is(0)))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].voteNum", is(0)))
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
                .andExpect(jsonPath("$.voteNum", is(0)))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_BY_INDEX_URL, 1)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("分类二")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(jsonPath("$.voteNum", is(0)))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_BY_INDEX_URL, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("分类三")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(jsonPath("$.voteNum", is(0)))
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
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].voteNum", is(0)))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 1, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类二")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类三")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].voteNum", is(0)))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 0, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].voteNum", is(0)))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].voteNum", is(0)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].voteNum", is(0)))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].voteNum", is(0)))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_add_one_rs_event_by_json() throws Exception {
        String requestJson = String.format("{\"eventName\":\"第四条事件\",\"keyword\":\"分类四\",\"userId\":\"%d\"}", dwightId);
        MvcResult mvcResult = mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_BY_INDEX_URL, 3)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第四条事件")))
                .andExpect(jsonPath("$.keyword", is("分类四")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(jsonPath("$.voteNum", is(0)))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);

        assertTrue(mvcResult.getResponse().containsHeader("index"));
        assertEquals("3", mvcResult.getResponse().getHeader("index"));
    }

    @Test
    public void should_update_third_rs_event() throws Exception {
        String requestJson = String.format("{\"eventName\":\"事件已更改\",\"keyword\":\"分类已更改\",\"userId\":\"%d\"}", dwightId);

        mockMvc.perform(put(String.format(PUT_ONE_RS_EVENT_URL, 2))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_BY_INDEX_URL, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("事件已更改")))
                .andExpect(jsonPath("$.keyword", is("分类已更改")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(jsonPath("$.voteNum", is(0)))
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
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].voteNum", is(0)))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_throw_bad_request_if_event_name_is_null_when_add_new_rs_event() throws Exception {
        RsEvent nameNullEvent = new RsEvent(null, "分类四", dwightId, 0);

        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(nameNullEvent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_bad_request_if_keyword_is_null_when_add_new_rs_event() throws Exception {
        RsEvent keywordNullEvent = new RsEvent("第四条事件", null, dwightId, 0);

        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(keywordNullEvent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_bad_request_if_user_is_null_when_add_new_rs_event() throws Exception {
        RsEvent userNullEvent = new RsEvent("第四条事件", "分类四", null, 0);

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
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    public void should_return_bad_request_when_user_param_invalid() throws Exception {
        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"eventName\":\"第四条事件\",\"keyword\":\"分类四\",\"user\":{\"user_name\":\"Dwight\",\"user_age\":200,\"user_gender\":\"male\",\"user_email\":\"michaelleqihust@gmail.com\",\"user_phone\":\"18706789189\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    public void should_return_ok_when_post_rs_event_with_new_api() throws Exception {
        int dwightID = userRepository.findByUserName(userDwight.getUserName()).get().getId();
        String requestJson = String.format("{\"eventName\":\"第四条事件\",\"keyword\":\"分类四\",\"userId\":\"%d\"}", dwightID);
        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isCreated());

        assertEquals(4L, rsEventRepository.count());
    }

    @Test
    public void should_return_bad_request_when_post_rs_event_if_user_id_not_exist() throws Exception {
        int invalidUserId = 10000;
        String requestJson = String.format("{\"eventName\":\"第四条事件\",\"keyword\":\"分类四\",\"userId\":\"%d\"}", invalidUserId);
        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_rs_event_when_patch_if_user_id_matches_rs_event_id() throws Exception {
        int firstEventId = rsEventRepository.findAll().get(0).getId();
        int dwightID = userRepository.findByUserName(userDwight.getUserName()).get().getId();
        String requestJson = String.format("{\"eventName\":\"已更新事件\",\"keyword\":\"分类已更新\",\"userId\":\"%d\"}", dwightID);
        mockMvc.perform(patch(String.format(PATCH_ONE_RS_EVENT_URL, firstEventId))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isOk());

        assertEquals("已更新事件", rsEventRepository.findById(firstEventId).get().getEventName());
        assertEquals("分类已更新", rsEventRepository.findById(firstEventId).get().getKeyword());
    }

    @Test
    public void should_update_rs_event_name_when_patch_if_user_id_matches_rs_event_id() throws Exception {
        int firstEventId = rsEventRepository.findAll().get(0).getId();
        int dwightID = userRepository.findByUserName(userDwight.getUserName()).get().getId();
        String requestJson = String.format("{\"eventName\":\"已更新事件\",\"userId\":\"%d\"}", dwightID);
        mockMvc.perform(patch(String.format(PATCH_ONE_RS_EVENT_URL, firstEventId))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isOk());

        assertEquals("已更新事件", rsEventRepository.findById(firstEventId).get().getEventName());
        assertEquals("分类一", rsEventRepository.findById(firstEventId).get().getKeyword());
    }

    @Test
    public void should_update_rs_event_keyword_when_patch_if_user_id_matches_rs_event_id() throws Exception {
        int firstEventId = rsEventRepository.findAll().get(0).getId();
        int dwightID = userRepository.findByUserName(userDwight.getUserName()).get().getId();
        String requestJson = String.format("{\"keyword\":\"分类已更新\",\"userId\":\"%d\"}", dwightID);
        mockMvc.perform(patch(String.format(PATCH_ONE_RS_EVENT_URL, firstEventId))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isOk());

        assertEquals("第一条事件", rsEventRepository.findById(firstEventId).get().getEventName());
        assertEquals("分类已更新", rsEventRepository.findById(firstEventId).get().getKeyword());
    }

    @Test
    public void should_return_bad_request_when_patch_if_user_id_not_matches_rs_event_id() throws Exception {
        int firstEventId = rsEventRepository.findAll().get(0).getId();
        int disMatchUserId = 1000;
        String requestJson = String.format("{\"keyword\":\"分类已更新\",\"userId\":\"%d\"}", disMatchUserId);
        mockMvc.perform(patch(String.format(PATCH_ONE_RS_EVENT_URL, firstEventId))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isBadRequest());

        assertEquals("第一条事件", rsEventRepository.findById(firstEventId).get().getEventName());
        assertEquals("分类一", rsEventRepository.findById(firstEventId).get().getKeyword());
    }
}