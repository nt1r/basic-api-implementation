package com.thoughtworks.rslist.util;

import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.pgleqi.RsEvent;
import com.thoughtworks.rslist.pgleqi.User;
import com.thoughtworks.rslist.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class Convertor {
    private Convertor() {}

    /* RsEvent */
    public static RsEventEntity convertRsEvent2RsEventEntity(UserRepository userRepository, RsEvent rsEvent) {
        int userId = userRepository.findByUserName(rsEvent.getUser().getUserName()).isPresent()
                ? userRepository.findByUserName(rsEvent.getUser().getUserName()).get().getID()
                : 0;
        return RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userId(String.valueOf(userId))
                .build();
    }

    public static RsEvent convertRsEventEntity2RsEvent(UserRepository userRepository, RsEventEntity rsEventEntity) {
        UserEntity userEntity = userRepository.findById(Integer.valueOf(rsEventEntity.getUserId())).get();
        return new RsEvent(rsEventEntity.getEventName(),
                rsEventEntity.getKeyword(),
                convertUserEntity2User(userEntity));
    }

    public static List<RsEvent> convertRsEventEntity2RsEvent(UserRepository userRepository, List<RsEventEntity> rsEventEntityList) {
        List<RsEvent> convertedResultList = new ArrayList<>();
        for (RsEventEntity rsEventEntity: rsEventEntityList) {
            convertedResultList.add(convertRsEventEntity2RsEvent(userRepository, rsEventEntity));
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
                .votes(10)
                .build();
    }
    /* User */
}
