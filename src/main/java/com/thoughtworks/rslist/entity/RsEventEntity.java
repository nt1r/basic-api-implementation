package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rs_event")
@Builder
public class RsEventEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String eventName;

    private String keyword;

    @Builder.Default
    private Integer voteNumSum = 0;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    private UserEntity userEntity;
}
