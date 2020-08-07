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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
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

    final User userDwight = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189");

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
        rsEventRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.save(convertUser2UserEntity(userDwight));

        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第一条事件", "分类一", userDwight)));
        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第二条事件", "分类二", userDwight)));
        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, new RsEvent("第三条事件", "分类三", userDwight)));
    }


    @Test
    public void should_vote_success_when_post_vote_if_user_has_enough_vote_num() throws Exception {
        int firstEventId = rsEventRepository.findAll().get(0).getId();
        int dwightID = userRepository.findByUserName(userDwight.getUserName()).get().getId();
        String requestJson = String.format("{\"voteNum\":\"5\",\"userId\":\"%d\",\"voteTime\":\"%s\"}", dwightID, LocalDate.now().toString());
        mockMvc.perform(post(String.format(POST_VOTE_FOR_RS_EVENT_URL, firstEventId))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isOk());
    }
}
