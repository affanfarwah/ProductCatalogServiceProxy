package com.example.ProductCatalogServiceProxy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
//    @ExceptionHandler({Exception.class}) // works for any type of exception
    private ResponseEntity<String> handleException() {
        return new ResponseEntity<String>("Kuch toh gadbad hai", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
