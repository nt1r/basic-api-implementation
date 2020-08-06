package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.component.CommonException;
import com.thoughtworks.rslist.component.GlobalExceptionHandler;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.ListRangeIndexException;
import com.thoughtworks.rslist.pgleqi.RsEvent;
import com.thoughtworks.rslist.pgleqi.User;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.thoughtworks.rslist.util.Convertor.convertRsEvent2RsEventEntity;
import static com.thoughtworks.rslist.util.Convertor.convertRsEventEntity2RsEvent;

@RestController
public class RsController {
    public static final String UNKNOWN_ERROR = "Unknown Error";
    public static final String INVALID_PARAM = "invalid param";
    public static final String RS_EVENT_NOT_EXIST = "rs event not exist";

    @Autowired
    public RsEventRepository rsEventRepository;

    @Autowired
    public UserRepository userRepository;

    ObjectMapper objectMapper;
    Logger logger = LoggerFactory.getLogger(RsController.class);

    public RsController() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        /*User userDwight = new User("Dwight", 25, "male", "michaelleqihust@gmail.com", "18706789189");
        rsEventRepository.save(convertRsEvent2RsEventEntity(new RsEvent("第一条事件", "分类一", userDwight)));
        rsEventRepository.save(convertRsEvent2RsEventEntity(new RsEvent("第二条事件", "分类二", userDwight)));
        rsEventRepository.save(convertRsEvent2RsEventEntity(new RsEvent("第三条事件", "分类三", userDwight)));*/
    }

    /*public RsEvent convertRsEventEntity2RsEvent(RsEventEntity rsEventEntity) {
        UserEntity userEntity = userRepository.findById(Integer.valueOf(rsEventEntity.getUserId())).get();
        return new RsEvent(rsEventEntity.getEventName(),
                rsEventEntity.getKeyword(),
                UserController.convertUserEntity2User(userEntity));
    }*/

    /*public RsEventEntity convertRsEvent2RsEventEntity(RsEvent rsEvent) {
        int userId = userRepository.findByUserName(rsEvent.getUser().getUserName()).isPresent()
                ? userRepository.findByUserName(rsEvent.getUser().getUserName()).get().getID()
                : 0;
        return RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userId(String.valueOf(userId))
                .build();
    }*/

    @GetMapping("/rs")
    public ResponseEntity getOneRsEventById(@RequestParam int id) throws IndexOutOfBoundsException {
        if (!rsEventRepository.existsById(id)) {
            throw new NoSuchElementException(RS_EVENT_NOT_EXIST);
        }
        RsEventEntity rsEventEntity = rsEventRepository.findById(id).get();
        return ResponseEntity.ok(convertRsEventEntity2RsEvent(userRepository, rsEventEntity));
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity getOneRsEventByIndex(@PathVariable int index) throws IndexOutOfBoundsException {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        if (!isIndexValid(index, rsEventEntityList)) {
            throw new NoSuchElementException(RS_EVENT_NOT_EXIST);
        }
        RsEventEntity rsEventEntity = rsEventEntityList.get(index);
        return ResponseEntity.ok(convertRsEventEntity2RsEvent(userRepository, rsEventEntity));
    }


    private boolean isIndexValid(int index, List<RsEventEntity> rsList) {
        return index >= 0 && index < rsList.size();
    }

    @GetMapping("/rs/list")
    public ResponseEntity getRsEventListBetweenIndexes(@RequestParam(required = false) Integer start,
                                                       @RequestParam(required = false) Integer end) throws ListRangeIndexException {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        if (start == null || end == null) {
            return ResponseEntity.ok(rsEventRepository.findAll());
        }
        if (!isRangeIndexValid(start, end, rsEventEntityList)) {
            // return GlobalExceptionHandler.handleCommonExceptions(new RuntimeException("invalid request param"));
            throw new ListRangeIndexException("invalid request param");
        }

        List<RsEvent> rsEventList = convertRsEventEntity2RsEvent(userRepository, rsEventEntityList);
        return ResponseEntity.ok(rsEventList.subList(start, end + 1));
    }

    private boolean isRangeIndexValid(Integer start, Integer end, List<RsEventEntity> rsList) {
        return isIndexValid(start, rsList) && isIndexValid(end, rsList) && start <= end;
    }

    @PostMapping("/rs")
    public ResponseEntity postOneRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, rsEvent));
        return generateResponseEntity(rsEvent, rsEventRepository.count() - 1, HttpStatus.CREATED);
    }

    @PutMapping("/rs/")
    public void putOneRsEvent(@RequestParam int index, @RequestBody @Valid RsEvent updateRsEvent) {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        assignRsEventFromUpdatedOne(rsEventEntityList.get(index), updateRsEvent);
        rsEventRepository.save(rsEventEntityList.get(index));
    }

    private void assignRsEventFromUpdatedOne(RsEventEntity originRsEvent, RsEvent updateRsEvent) {
        originRsEvent.setEventName(updateRsEvent.getEventName());
        originRsEvent.setKeyword(updateRsEvent.getKeyword());
    }

    @DeleteMapping("/rs/")
    public void deleteOneRsEvent(@RequestParam int index) {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        rsEventRepository.deleteById(rsEventEntityList.get(index).getId());
    }

    @PostMapping("/rs/event")
    public ResponseEntity postOneRsEventNew(@RequestBody @Valid RsEventEntity rsEventEntity) {
        rsEventRepository.save(rsEventEntity);
        return generateResponseEntity(rsEventEntity, rsEventRepository.count() - 1, HttpStatus.CREATED);
    }
    /* ====== */

    private ResponseEntity generateResponseEntity(Object body, long index, HttpStatus statusCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("index", String.valueOf(index));
        return new ResponseEntity(body, httpHeaders, statusCode);
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
