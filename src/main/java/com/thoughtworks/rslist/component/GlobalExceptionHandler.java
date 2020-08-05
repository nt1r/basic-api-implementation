package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exceptions.ListRangeIndexException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    final String METHOD_ARGUMENT_NOT_VALID_MESSAGE = "invalid param";

    @ExceptionHandler({IndexOutOfBoundsException.class, ListRangeIndexException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<CommonException> handleCommonExceptions(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException) {
            return ResponseEntity.badRequest().body(new CommonException(METHOD_ARGUMENT_NOT_VALID_MESSAGE));
        }
        return ResponseEntity.badRequest().body(new CommonException(exception.getMessage()));
    }
}
