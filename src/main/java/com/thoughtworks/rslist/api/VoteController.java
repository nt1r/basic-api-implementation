package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
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

import static com.thoughtworks.rslist.util.Generator.generateResponseEntity;


@RestController
public class VoteController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RsEventRepository rsEventRepository;

    @Autowired
    public VoteRepository voteRepository;

    /*@PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteForRsEvent(@PathVariable int rsEventId, @RequestBody VoteEntity voteEntity) throws Exception {
        UserEntity userEntity = voteEntity.getUserEntity();
        if (isUserHasEnoughVoteNum(userEntity, voteEntity.getVoteNum())) {
            userEntity.setVoteNumLeft(userEntity.getVoteNumLeft() - voteEntity.getVoteNum());
            userRepository.save(userEntity);

            voteRepository.save(voteEntity);

            return generateResponseEntity(voteEntity, -1, HttpStatus.OK);
        } else {
            throw new Exception("User Not Enough Vote Number");
        }
    }*/

    private boolean isUserHasEnoughVoteNum(UserEntity userEntity, int voteNum) {
        return userEntity != null && userEntity.getVoteNumLeft() >= voteNum;
    }
}
