package com.thoughtworks.rslist.util;

import com.thoughtworks.rslist.pgleqi.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Generator {
    private Generator() {}

    public static ResponseEntity generateResponseEntity(Object object, long index, HttpStatus statusCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("index", String.valueOf(index));
        return new ResponseEntity<>(object, httpHeaders, statusCode);
    }
}
