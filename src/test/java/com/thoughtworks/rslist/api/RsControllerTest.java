package com.thoughtworks.rslist.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    final String GET_ONE_RS_EVENT_URL = "/rs/%d";
    final String GET_MULTIPLE_RS_EVENT_URL = "/rs/list?start=%d&end=%d";

    @Autowired
    MockMvc mockMvc;

    @Test
    public void should_get_one_rs_event_by_index() throws Exception {
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 0)).accept(MediaType.TEXT_PLAIN))
                .andExpect(content().string("第一条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 1)).accept(MediaType.TEXT_PLAIN))
                .andExpect(content().string("第二条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_ONE_RS_EVENT_URL, 2)).accept(MediaType.TEXT_PLAIN))
                .andExpect(content().string("第三条事件"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_multiple_rs_event_by_indexes() throws Exception {
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 0, 1)).accept(MediaType.TEXT_PLAIN))
                .andExpect(content().string("[第一条事件, 第二条事件]"))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 1, 2)).accept(MediaType.TEXT_PLAIN))
                .andExpect(content().string("[第二条事件, 第三条事件]"))
                .andExpect(status().isOk());
        mockMvc.perform(get(String.format(GET_MULTIPLE_RS_EVENT_URL, 0, 2)).accept(MediaType.TEXT_PLAIN))
                .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件]"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list").accept(MediaType.TEXT_PLAIN))
                .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件]"))
                .andExpect(status().isOk());
    }
}