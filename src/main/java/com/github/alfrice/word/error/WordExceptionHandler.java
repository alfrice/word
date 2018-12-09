package com.github.alfrice.word.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by alice.martin
 * Developer: alice.martin
 * Date: 12/8/18
 * Time: 2:17 PM
 * Description: com.github.alfrice.word.error
 */
@ControllerAdvice
public class WordExceptionHandler
        extends ResponseEntityExceptionHandler {

    /*@ExceptionHandler(value
            = {WordException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }*/
}