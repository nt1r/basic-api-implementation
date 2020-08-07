package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.pgleqi.RsEvent;
import com.thoughtworks.rslist.pgleqi.User;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.thoughtworks.rslist.util.Convertor.convertRsEvent2RsEventEntity;
import static com.thoughtworks.rslist.util.Convertor.convertUser2UserEntity;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    final String POST_VOTE_FOR_RS_EVENT_URL = "/rs/vote/%d";
    final String GET_VOTE_LIST_URL = "/rs/vote/list?startDate=%s&endDate=%s";

    final User userDwight = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189");
    int dwightId;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();

        dwightId = userRepository.save(convertUser2UserEntity(userDwight)).getId();

        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第一条事件", "分类一", dwightId, 0)));
        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第二条事件", "分类二", dwightId, 0)));
        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第三条事件", "分类三", dwightId, 0)));
    }


    @Test
    public void should_vote_success_when_post_vote_if_user_has_enough_vote_num() throws Exception {
        int firstEventId = rsEventRepository.findAll().get(0).getId();
        int voteNum = 5;
        String requestJson = String.format("{\"voteNum\":\"%d\",\"userId\":\"%d\",\"voteTime\":\"%s\"}", voteNum, dwightId, LocalDate.now().toString());
        mockMvc.perform(post(String.format(POST_VOTE_FOR_RS_EVENT_URL, firstEventId))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isOk());

        assertEquals(1L, voteRepository.count());
        assertEquals(5, voteRepository.findAll().get(0).getVoteNum());
    }

    @Test
    public void should_vote_failed_when_post_vote_if_user_has_not_enough_vote_num() throws Exception {
        int firstEventId = rsEventRepository.findAll().get(0).getId();
        int voteNum = 20;
        String requestJson = String.format("{\"voteNum\":\"%d\",\"userId\":\"%d\",\"voteTime\":\"%s\"}", voteNum, dwightId, LocalDate.now().toString());
        mockMvc.perform(post(String.format(POST_VOTE_FOR_RS_EVENT_URL, firstEventId))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isBadRequest());

        assertEquals(0L, voteRepository.count());
    }

    @Test
    public void should_get_vote_list_between_start_date_and_end_date() throws Exception {
        int firstEventId = rsEventRepository.findAll().get(0).getId();
        LocalDate voteDateStart = LocalDate.parse("2020-03-01");
        LocalDate voteDateEnd = LocalDate.parse("2020-04-01");
        String requestJsonStartVote = String.format("{\"voteNum\":\"%d\",\"userId\":\"%d\",\"voteTime\":\"%s\"}", 3, dwightId, voteDateStart);
        String requestJsonEndVote = String.format("{\"voteNum\":\"%d\",\"userId\":\"%d\",\"voteTime\":\"%s\"}", 4, dwightId, voteDateEnd);

        mockMvc.perform(post(String.format(POST_VOTE_FOR_RS_EVENT_URL, firstEventId))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJsonStartVote))
                .andExpect(status().isOk());
        mockMvc.perform(post(String.format(POST_VOTE_FOR_RS_EVENT_URL, firstEventId))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJsonEndVote))
                .andExpect(status().isOk());
        assertEquals(2L, voteRepository.count());

        mockMvc.perform(get(String.format(GET_VOTE_LIST_URL, "2020-01-01", "2021-01-01"))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].voteNum", is(3)))
                .andExpect(jsonPath("$.[0].userId", is(dwightId)))
                .andExpect(jsonPath("$.[1].voteNum", is(4)))
                .andExpect(jsonPath("$.[1].userId", is(dwightId)));
    }
}
