package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.component.CommonException;
import com.thoughtworks.rslist.exceptions.ListRangeIndexException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.service.RsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RsController {
    public static final String UNKNOWN_ERROR = "Unknown Error";
    public static final String INVALID_PARAM = "invalid param";

    ObjectMapper objectMapper;
    Logger logger = LoggerFactory.getLogger(RsController.class);

    private final RsService rsService;

    public RsController(RsService rsService) {
        objectMapper = new ObjectMapper();
        this.rsService = rsService;
    }

    @GetMapping("/rs")
    public ResponseEntity getOneRsEventById(@RequestParam int id) {
        return rsService.getOneRsEventById(id);
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity getOneRsEventByIndex(@PathVariable int index) {
        return rsService.getOneRsEventByIndex(index);
    }

    @GetMapping("/rs/list")
    public ResponseEntity getRsEventListBetweenIndexes(@RequestParam(required = false) Integer start,
                                                       @RequestParam(required = false) Integer end) throws ListRangeIndexException {
        return rsService.getRsEventListBetweenIndexes(start, end);
    }

    @PostMapping("/rs")
    public ResponseEntity postOneRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        return rsService.postOneRsEvent(rsEvent);
    }

    @PutMapping("/rs/")
    public void putOneRsEvent(@RequestParam int index, @RequestBody @Valid RsEvent updateRsEvent) {
        rsService.putOneRsEvent(index, updateRsEvent);
    }

    @DeleteMapping("/rs/")
    public void deleteOneRsEvent(@RequestParam int index) {
        rsService.deleteOneRsEvent(index);
    }

    @PatchMapping(path = "/rs/{rsEventId}")
    public ResponseEntity patchOneRsEvent(@PathVariable int rsEventId, @RequestBody RsEvent rsEvent) {
        return rsService.patchOneRsEvent(rsEventId, rsEvent);
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
