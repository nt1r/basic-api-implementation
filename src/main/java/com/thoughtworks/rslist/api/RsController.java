package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.component.CommonException;
import com.thoughtworks.rslist.component.GlobalExceptionHandler;
import com.thoughtworks.rslist.exceptions.ListRangeIndexException;
import com.thoughtworks.rslist.pgleqi.RsEvent;
import com.thoughtworks.rslist.pgleqi.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    public static final String UNKNOWN_ERROR = "Unknown Error";
    public static final String INVALID_PARAM = "invalid param";
    public static List<RsEvent> rsList = new ArrayList<>();
    ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(RsController.class);

    public RsController() throws JsonProcessingException {
        User userDwight = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189");
        rsList.add(new RsEvent("第一条事件", "分类一", userDwight));
        rsList.add(new RsEvent("第二条事件", "分类二", userDwight));
        rsList.add(new RsEvent("第三条事件", "分类三", userDwight));

        objectMapper = new ObjectMapper();
        // System.out.println(objectMapper.writeValueAsString(rsList.get(0)));
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity getOneRsEventByIndex(@PathVariable int index) throws IndexOutOfBoundsException {
        if (!isIndexValid(index, rsList)) {
            throw new IndexOutOfBoundsException("invalid index");
            // return GlobalExceptionHandler.handleCommonExceptions(new IndexOutOfBoundsException("invalid index"));
        }
        return ResponseEntity.ok(rsList.get(index));
    }

    private boolean isIndexValid(int index, List<RsEvent> rsList) {
        return index >= 0 && index < rsList.size();
    }

    @GetMapping("/rs/list")
    public ResponseEntity getRsEventListBetweenIndexes(@RequestParam(required = false) Integer start,
                                                       @RequestParam(required = false) Integer end) throws ListRangeIndexException {
        if (start == null || end == null) {
            return ResponseEntity.ok(rsList);
        }
        if (!isRangeIndexValid(start, end, rsList)) {
            // return GlobalExceptionHandler.handleCommonExceptions(new RuntimeException("invalid request param"));
            throw new ListRangeIndexException("invalid request param");
        }

        return ResponseEntity.ok(rsList.subList(start, end + 1));
    }

    private boolean isRangeIndexValid(Integer start, Integer end, List<RsEvent> rsList) {
        return isIndexValid(start, rsList) && isIndexValid(end, rsList) && start <= end;
    }

    @PostMapping("/rs")
    public ResponseEntity postOneRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        rsList.add(rsEvent);
        if (!UserController.userList.contains(rsEvent.getUser())) {
            UserController.userList.add(rsEvent.getUser());
        }
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

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommonException> handleCommonExceptions(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException) {
            logger.error(INVALID_PARAM);
            return ResponseEntity.badRequest().body(new CommonException(INVALID_PARAM));
        } else {
            logger.error(UNKNOWN_ERROR);
            throw new RuntimeException(UNKNOWN_ERROR);
        }
    }
}
