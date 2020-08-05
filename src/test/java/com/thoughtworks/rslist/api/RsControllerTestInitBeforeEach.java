package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.pgleqi.RsEvent;
import com.thoughtworks.rslist.pgleqi.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class RsControllerTestInitBeforeEach {
    final String GET_ONE_RS_EVENT_URL = "/rs/%d";
    final String GET_MULTIPLE_RS_EVENT_URL = "/rs/list?start=%d&end=%d";
    final String POST_ONE_RS_EVENT_URL = "/rs";
    final String PUT_ONE_RS_EVENT_URL = "/rs?index=%d";
    final String DELETE_ONE_RS_EVENT_URL = "/rs?index=%d";

    final User userDwight = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189");
    final ObjectMapper objectMapper = new ObjectMapper();

    MockMvc mockMvc;

    @BeforeEach
    public void init() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
    }

    @Test
    public void should_receive_string_list_with_200_ok() throws Exception {
        mockMvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(jsonPath("$[2]", hasKey("user")))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_get_one_rs_event_by_index() throws Exception {
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 0)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyword", is("分类一")))
                .andExpect(jsonPath("$", hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 1)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("分类二")))
                .andExpect(jsonPath("$", hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("分类三")))
                .andExpect(jsonPath("$", hasKey("user")))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_get_multiple_rs_event_by_indexes() throws Exception {
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 0, 1)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 1, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类二")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类三")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 0, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(jsonPath("$[2]", hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(jsonPath("$[2]", hasKey("user")))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_add_one_rs_event_by_json() throws Exception {
        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(new RsEvent("第四条事件", "分类四", userDwight))))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 3)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第四条事件")))
                .andExpect(jsonPath("$.keyword", is("分类四")))
                .andExpect(status().isOk());
        // System.out.println(mockMvc);
    }

    @Test
    public void should_update_third_rs_event() throws Exception {
        mockMvc.perform(put(String.format(PUT_ONE_RS_EVENT_URL, 2))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(new RsEvent("事件已更改", "分类已更改", userDwight))))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("事件已更改")))
                .andExpect(jsonPath("$.keyword", is("分类已更改")))
                .andExpect(jsonPath("$", hasKey("user")))
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
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
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
}