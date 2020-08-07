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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.thoughtworks.rslist.util.Convertor.convertVote2VoteEntity;
import static com.thoughtworks.rslist.util.Generator.generateResponseEntity;


@RestController
public class VoteController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RsEventRepository rsEventRepository;

    @Autowired
    public VoteRepository voteRepository;

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
}
