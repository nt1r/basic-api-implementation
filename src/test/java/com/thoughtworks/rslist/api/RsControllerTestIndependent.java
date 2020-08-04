package com.thoughtworks.rslist.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RsControllerTestIndependent {
    final String GET_ONE_RS_EVENT_URL = "/rs/%d";
    final String GET_MULTIPLE_RS_EVENT_URL = "/rs/list?start=%d&end=%d";
    final String POST_ONE_RS_EVENT_URL = "/rs";
    final String PUT_ONE_RS_EVENT_URL = "/rs?index=%d";
    final String DELETE_ONE_RS_EVENT_URL = "/rs?index=%d";

    @Autowired
    MockMvc mockMvc;

    @Test
    public void should_receive_string_list_with_200_ok() throws Exception {
        mockMvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_one_rs_event_by_index() throws Exception {
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 0)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyword", is("分类一")))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 1)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("分类二")))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("分类三")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_multiple_rs_event_by_indexes() throws Exception {
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 0, 1)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 1, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类二")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类三")))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 0, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("分类三")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_add_one_rs_event_by_json() throws Exception {
        mockMvc.perform(post(POST_ONE_RS_EVENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"eventName\":\"第四条事件\",\"keyword\":\"分类四\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 3)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("第四条事件")))
                .andExpect(jsonPath("$.keyword", is("分类四")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_update_third_rs_event() throws Exception {
        mockMvc.perform(put(String.format(PUT_ONE_RS_EVENT_URL, 2))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"eventName\":\"事件已更改\",\"keyword\":\"分类已更改\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 2)).accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.eventName", is("事件已更改")))
                .andExpect(jsonPath("$.keyword", is("分类已更改")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_delete_third_rs_event() throws Exception {
        mockMvc.perform(delete(String.format(DELETE_ONE_RS_EVENT_URL, 2)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list").accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("分类一")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类二")))
                .andExpect(status().isOk());
    }
}