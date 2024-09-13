package com.example.assignment.util;


import com.example.assignment.dto.GenericServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ResultUtil {
    //util method to generate custom response and set custom statuscode and msg
    //currently it is in basic form
    public static ResponseEntity<Object> generateResponse(Object data, HttpStatus status) {
        return new ResponseEntity<>(GenericServerResponse.builder().data(data).build(),status);
    }
}

