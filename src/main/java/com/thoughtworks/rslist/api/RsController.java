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
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.thoughtworks.rslist.util.Convertor.convertRsEvent2RsEventEntity;
import static com.thoughtworks.rslist.util.Convertor.convertRsEventEntity2RsEvent;
import static com.thoughtworks.rslist.util.Generator.generateResponseEntity;

@RestController
public class RsController {
    public static final String UNKNOWN_ERROR = "Unknown Error";
    public static final String INVALID_PARAM = "invalid param";
    public static final String RS_EVENT_NOT_EXIST = "rs event not exist";

    @Autowired
    private RsEventRepository rsEventRepository;

    @Autowired
    private UserRepository userRepository;

    ObjectMapper objectMapper;
    Logger logger = LoggerFactory.getLogger(RsController.class);

    public RsController() {
        objectMapper = new ObjectMapper();
    }

    @GetMapping("/rs")
    public ResponseEntity getOneRsEventById(@RequestParam int id) throws IndexOutOfBoundsException {
        if (!rsEventRepository.existsById(id)) {
            throw new NoSuchElementException(RS_EVENT_NOT_EXIST);
        }
        RsEventEntity rsEventEntity = rsEventRepository.findById(id).get();
        return ResponseEntity.ok(convertRsEventEntity2RsEvent(rsEventEntity));
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity getOneRsEventByIndex(@PathVariable int index) throws IndexOutOfBoundsException {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        if (!isIndexValid(index, rsEventEntityList)) {
            throw new NoSuchElementException(RS_EVENT_NOT_EXIST);
        }
        RsEventEntity rsEventEntity = rsEventEntityList.get(index);
        return ResponseEntity.ok(convertRsEventEntity2RsEvent(rsEventEntity));
    }


    private boolean isIndexValid(int index, List<RsEventEntity> rsList) {
        return index >= 0 && index < rsList.size();
    }

    @GetMapping("/rs/list")
    public ResponseEntity getRsEventListBetweenIndexes(@RequestParam(required = false) Integer start,
                                                       @RequestParam(required = false) Integer end) throws ListRangeIndexException {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        if (start == null || end == null) {
            return ResponseEntity.ok(convertRsEventEntity2RsEvent(rsEventEntityList));
        }
        if (!isRangeIndexValid(start, end, rsEventEntityList)) {
            // return GlobalExceptionHandler.handleCommonExceptions(new RuntimeException("invalid request param"));
            throw new ListRangeIndexException("invalid request param");
        }

        List<RsEvent> rsEventList = convertRsEventEntity2RsEvent(rsEventEntityList);
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

    @PatchMapping(path = "/rs/{rsEventId}")
    public ResponseEntity patchOneRsEvent(@PathVariable int rsEventId, @RequestBody RsEvent rsEvent) {
        RsEventEntity rsEventEntityInDB = rsEventRepository.findById(rsEventId).get();
        if (rsEvent.getUserId().equals(rsEventEntityInDB.getUserEntity().getId())) {
            if (rsEvent.getEventName() != null) {
                rsEventEntityInDB.setEventName(rsEvent.getEventName());
            }
            if (rsEvent.getKeyword() != null) {
                rsEventEntityInDB.setKeyword(rsEvent.getKeyword());
            }
            rsEventRepository.save(rsEventEntityInDB);
            return generateResponseEntity(rsEventEntityInDB, rsEventId, HttpStatus.OK);
        } else {
            return generateResponseEntity(rsEventEntityInDB, rsEventId, HttpStatus.BAD_REQUEST);
        }
    }

    /* ====== */

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
