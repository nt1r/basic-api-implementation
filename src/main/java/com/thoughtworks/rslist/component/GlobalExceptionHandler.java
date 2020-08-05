package com.thoughtworks.rslist.component;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IndexOutOfBoundsException.class, RuntimeException.class})
    public ResponseEntity<CommonException> handleCommonExceptions(Exception exception) {
        return ResponseEntity.badRequest().body(new CommonException(exception.getMessage()));
    }
}
