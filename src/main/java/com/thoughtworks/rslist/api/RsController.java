package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.pgleqi.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    ObjectMapper objectMapper;
    private List<RsEvent> rsList;

    public RsController() throws JsonProcessingException {
        rsList = new ArrayList<>();
        rsList.add(new RsEvent("第一条事件", "分类一"));
        rsList.add(new RsEvent("第二条事件", "分类二"));
        rsList.add(new RsEvent("第三条事件", "分类三"));

        objectMapper = new ObjectMapper();
        // System.out.println(objectMapper.writeValueAsString(rsList.get(0)));
    }

    @GetMapping("/rs/{index}")
    public RsEvent getOneRsEventByIndex(@PathVariable int index) {
        return rsList.get(index);
    }

    @GetMapping("/rs/list")
    public List<RsEvent> getRsEventListBetweenIndexes(@RequestParam(defaultValue = "0") int start,
                                                      @RequestParam(defaultValue = "2") int end) {
        return rsList.subList(start, end + 1);
    }

    @PostMapping("/rs")
    public void postOneRsEvent(@RequestBody RsEvent rsEvent) {
        rsList.add(rsEvent);
    }
}
