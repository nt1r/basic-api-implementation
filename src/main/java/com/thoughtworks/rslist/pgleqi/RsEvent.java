package com.thoughtworks.rslist.pgleqi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RsEvent {
    @NotNull
    private String eventName;

    @NotNull
    private String keyword;

    @NotNull
    private Integer userId;

    public RsEvent() {
    }

    public RsEvent(String eventName, String keyword, Integer userId) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
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
}
