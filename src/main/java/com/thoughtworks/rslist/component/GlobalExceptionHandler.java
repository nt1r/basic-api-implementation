package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exceptions.ListRangeIndexException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IndexOutOfBoundsException.class, ListRangeIndexException.class})
    public ResponseEntity<CommonException> handleCommonExceptions(Exception exception) {
        return ResponseEntity.badRequest().body(new CommonException(exception.getMessage()));
    }
}
