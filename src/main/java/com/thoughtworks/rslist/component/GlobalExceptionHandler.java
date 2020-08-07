package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.exceptions.ListRangeIndexException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(RsController.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CommonException> handleCommonExceptions(Exception exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new CommonException(exception.getMessage()));
    }
}
