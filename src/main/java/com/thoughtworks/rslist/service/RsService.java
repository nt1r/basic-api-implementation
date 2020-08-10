package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exceptions.ListRangeIndexException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.NoSuchElementException;

import static com.thoughtworks.rslist.util.Convertor.convertRsEvent2RsEventEntity;
import static com.thoughtworks.rslist.util.Convertor.convertRsEventEntity2RsEvent;
import static com.thoughtworks.rslist.util.Generator.generateResponseEntity;

public class RsService {
    public static final String RS_EVENT_NOT_EXIST = "rs event not exist";

    private final RsEventRepository rsEventRepository;

    private final UserRepository userRepository;

    public RsService(RsEventRepository rsEventRepository, UserRepository userRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        // some init logic here
    }

    /* ====== API Methods ====== */
    public ResponseEntity getOneRsEventById(int eventId) {
        boolean isEventExist = rsEventRepository.existsById(eventId);
        if (!isEventExist) {
            throw new NoSuchElementException(RS_EVENT_NOT_EXIST);
        }
        RsEventEntity rsEventEntity = rsEventRepository.findById(eventId).get();
        return ResponseEntity.ok(convertRsEventEntity2RsEvent(rsEventEntity));
    }

    public ResponseEntity getOneRsEventByIndex(int index) {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        if (!isIndexValid(index, rsEventEntityList)) {
            throw new NoSuchElementException(RS_EVENT_NOT_EXIST);
        }
        RsEventEntity rsEventEntity = rsEventEntityList.get(index);
        return ResponseEntity.ok(convertRsEventEntity2RsEvent(rsEventEntity));
    }

    public ResponseEntity getRsEventListBetweenIndexes(Integer start, Integer end) throws ListRangeIndexException {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        if (start == null || end == null) {
            return ResponseEntity.ok(convertRsEventEntity2RsEvent(rsEventEntityList));
        }
        if (!isRangeIndexValid(start, end, rsEventEntityList)) {
            throw new ListRangeIndexException("invalid request param");
        }

        List<RsEvent> rsEventList = convertRsEventEntity2RsEvent(rsEventEntityList);
        return ResponseEntity.ok(rsEventList.subList(start, end + 1));
    }

    public ResponseEntity postOneRsEvent(RsEvent rsEvent) {
        rsEventRepository.save(convertRsEvent2RsEventEntity(userRepository, rsEvent));
        return generateResponseEntity(rsEvent, rsEventRepository.count() - 1, HttpStatus.CREATED);
    }

    public void putOneRsEvent(int index, RsEvent updateRsEvent) {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        assignRsEventFromUpdatedOne(rsEventEntityList.get(index), updateRsEvent);
        rsEventRepository.save(rsEventEntityList.get(index));
    }

    public void deleteOneRsEvent(int index) {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        rsEventRepository.deleteById(rsEventEntityList.get(index).getId());
    }

    public ResponseEntity patchOneRsEvent(int rsEventId, RsEvent rsEvent) {
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
    /* ====== API Methods ====== */

    private boolean isIndexValid(int index, List<RsEventEntity> rsList) {
        return index >= 0 && index < rsList.size();
    }

    private boolean isRangeIndexValid(Integer start, Integer end, List<RsEventEntity> rsList) {
        return isIndexValid(start, rsList) && isIndexValid(end, rsList) && start <= end;
    }

    private void assignRsEventFromUpdatedOne(RsEventEntity originRsEvent, RsEvent updateRsEvent) {
        originRsEvent.setEventName(updateRsEvent.getEventName());
        originRsEvent.setKeyword(updateRsEvent.getKeyword());
    }
}
