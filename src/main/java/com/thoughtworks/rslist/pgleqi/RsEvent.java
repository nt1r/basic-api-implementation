package com.thoughtworks.rslist.pgleqi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class RsEvent {
    @NotNull
    private String eventName;

    @NotNull
    private String keyword;

    @NotNull
    private Integer userId;

    @JsonProperty("voteNum")
    private Integer voteNumSum;

    public RsEvent() {
    }

    public RsEvent(String eventName, String keyword, Integer userId, Integer voteNum) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
        this.voteNumSum = voteNum;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @JsonIgnore
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVoteNumSum() {
        return voteNumSum;
    }

    public void setVoteNumSum(Integer voteNumSum) {
        this.voteNumSum = voteNumSum;
    }
}
