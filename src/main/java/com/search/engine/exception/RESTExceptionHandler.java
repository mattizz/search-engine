package com.search.engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RESTExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BadFileException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleBadFileException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException() {
        return new ResponseEntity<>("Internal problems", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {MultipartException.class})
    public ResponseEntity<?> handleTooBigFileException(Exception ex) {
        return new ResponseEntity<>("File is too big to upload", HttpStatus.PAYLOAD_TOO_LARGE);
    }
}
