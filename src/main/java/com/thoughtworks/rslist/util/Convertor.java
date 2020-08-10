package com.thoughtworks.rslist.util;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Convertor {
    private Convertor() {}

    /* RsEvent */
    public static RsEventEntity convertRsEvent2RsEventEntity(UserRepository userRepository, RsEvent rsEvent) {
        return RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .voteNumSum(0)
                .userId(rsEvent.getUserId())
                .userEntity(userRepository.findById(rsEvent.getUserId()).get())
                .build();
    }

    public static RsEvent convertRsEventEntity2RsEvent(RsEventEntity rsEventEntity) {
        UserEntity userEntity = rsEventEntity.getUserEntity();
        return new RsEvent(rsEventEntity.getEventName(),
                rsEventEntity.getKeyword(),
                userEntity.getId(),
                rsEventEntity.getVoteNumSum());
    }

    public static List<RsEvent> convertRsEventEntity2RsEvent(List<RsEventEntity> rsEventEntityList) {
        List<RsEvent> convertedResultList = new ArrayList<>();
        for (RsEventEntity rsEventEntity: rsEventEntityList) {
            convertedResultList.add(convertRsEventEntity2RsEvent(rsEventEntity));
        }
        return convertedResultList;
    }
    /* RsEvent */

    /* User */
    public static List<User> convertUserEntity2User(List<UserEntity> userEntityList) {
        List<User> convertedResultList = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            convertedResultList.add(convertUserEntity2User(userEntity));
        }
        return convertedResultList;
    }

    public static User convertUserEntity2User(UserEntity userEntity) {
        return new User(userEntity.getUserName(),
                userEntity.getAge(),
                userEntity.getGender(),
                userEntity.getEmail(),
                userEntity.getPhone());
    }

    public static UserEntity convertUser2UserEntity(User user) {
        return UserEntity.builder()
                .userName(user.getUserName())
                .age(user.getAge())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .voteNumLeft(10)
                .build();
    }
    /* User */

    /* Vote */
    public static Vote convertVoteEntity2Vote(VoteEntity voteEntity) {
        return new Vote(voteEntity.getVoteNum(), voteEntity.getUserEntity().getId(), voteEntity.getVoteTime().toString());
    }

    public static VoteEntity convertVote2VoteEntity(Vote vote) {
        return VoteEntity.builder()
                .voteNum(vote.getVoteNum())
                .voteTime(LocalDate.parse(vote.getVoteTime()))
                .build();
    }

    public static List<Vote> convertVoteEntity2Vote(List<VoteEntity> voteEntityList) {
        List<Vote> voteList = new ArrayList<>();
        for (VoteEntity voteEntity: voteEntityList) {
            voteList.add(convertVoteEntity2Vote(voteEntity));
        }
        return voteList;
    }
    /* Vote */
}
