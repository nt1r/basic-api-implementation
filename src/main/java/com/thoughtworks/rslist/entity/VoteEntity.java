package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vote")
@Builder
public class VoteEntity {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer voteNum;

    private LocalDate voteTime;

    private Integer userId;

    private Integer rsEventId;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "e_id")
    private RsEventEntity rsEventEntity;
}
