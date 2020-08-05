package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.pgleqi.RsEvent;
import com.thoughtworks.rslist.pgleqi.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    ObjectMapper objectMapper;
    private List<RsEvent> rsList;

    public RsController() throws JsonProcessingException {
        User userDwight = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189");
        rsList = new ArrayList<>();
        rsList.add(new RsEvent("第一条事件", "分类一", userDwight));
        rsList.add(new RsEvent("第二条事件", "分类二", userDwight));
        rsList.add(new RsEvent("第三条事件", "分类三", userDwight));

        objectMapper = new ObjectMapper();
        // System.out.println(objectMapper.writeValueAsString(rsList.get(0)));
    }

    @GetMapping("/rs/{index}")
    public RsEvent getOneRsEventByIndex(@PathVariable int index) {
        return rsList.get(index);
    }

    @GetMapping("/rs/list")
    public List<RsEvent> getRsEventListBetweenIndexes(@RequestParam(required = false) Integer start,
                                                      @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return rsList;
        }
        return rsList.subList(start, end + 1);
    }

    @PostMapping("/rs")
    public ResponseEntity<RsEvent> postOneRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        rsList.add(rsEvent);
        return generateResponseEntity(rsEvent, rsList.size() - 1, HttpStatus.CREATED);
    }

    @PutMapping("/rs")
    public void putOneRsEvent(@RequestParam int index, @RequestBody @Valid RsEvent updateRsEvent) {
        assignRsEventFromUpdatedOne(rsList.get(index), updateRsEvent);
    }

    private void assignRsEventFromUpdatedOne(RsEvent originRsEvent, RsEvent updateRsEvent) {
        originRsEvent.setEventName(updateRsEvent.getEventName());
        originRsEvent.setKeyword(updateRsEvent.getKeyword());
        originRsEvent.setUser(updateRsEvent.getUser());
    }

    @DeleteMapping("/rs")
    public void deleteOneRsEvent(@RequestParam int index) {
        rsList.remove(index);
        // System.out.println(rsList.size());
    }

    private ResponseEntity<RsEvent> generateResponseEntity(RsEvent rsEvent, int index, HttpStatus statusCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("index", String.valueOf(index));
        return new ResponseEntity<>(rsEvent, httpHeaders, statusCode);
    }
}
