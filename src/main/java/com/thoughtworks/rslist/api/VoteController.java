package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.pgleqi.Vote;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.thoughtworks.rslist.util.Convertor.*;
import static com.thoughtworks.rslist.util.Generator.generateResponseEntity;


@RestController
public class VoteController {

    public final UserRepository userRepository;

    public final RsEventRepository rsEventRepository;

    public final VoteRepository voteRepository;

    public VoteController(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteForRsEvent(@PathVariable int rsEventId, @RequestBody Vote vote) throws Exception {
        UserEntity userEntity = userRepository.findById(vote.getUserId()).get();
        RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId).get();
        if (isUserHasEnoughVoteNum(userEntity, vote.getVoteNum())) {
            userEntity.setVoteNumLeft(userEntity.getVoteNumLeft() - vote.getVoteNum());
            userRepository.save(userEntity);

            rsEventEntity.setVoteNumSum(rsEventEntity.getVoteNumSum() + vote.getVoteNum());
            rsEventRepository.save(rsEventEntity);

            VoteEntity voteEntity = convertVote2VoteEntity(vote);
            voteEntity.setUserEntity(userEntity);
            voteEntity.setRsEventEntity(rsEventEntity);
            voteRepository.save(voteEntity);

            return generateResponseEntity(voteEntity, -1, HttpStatus.OK);
        } else {
            throw new Exception("User Not Enough Vote Number");
        }
    }

    private boolean isUserHasEnoughVoteNum(UserEntity userEntity, int voteNum) {
        return userEntity != null && userEntity.getVoteNumLeft() >= voteNum;
    }

    @GetMapping("/rs/vote/list")
    public ResponseEntity findVoteListBetweenDates(@RequestParam String startDate, @RequestParam String endDate) {
        List<VoteEntity> voteEntityList = voteRepository.findByStartDateAndEndDate(startDate, endDate);
        return generateResponseEntity(convertVoteEntity2Vote(voteEntityList), -1, HttpStatus.OK);
    }
}
