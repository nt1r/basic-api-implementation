package com.thoughtworks.rslist.pgleqi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Vote {
    private Integer voteNum;
    private Integer userId;
    private String voteTime;

    public Vote(Integer voteNum, Integer userId, String voteTime) {
        this.voteNum = voteNum;
        this.userId = userId;
        this.voteTime = voteTime;
    }
}
