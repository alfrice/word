package com.github.alfrice.word.controller;

import com.github.alfrice.word.error.WordException;
import com.github.alfrice.word.service.WordService;
import com.github.alfrice.word.util.WordConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * Created by alice.martin
 * Developer: alice.martin
 * Date: 12/2/18
 * Time: 10:18 AM
 * Description: Rest controller for word
 */
@RestController
@ControllerAdvice
@Slf4j
public class WordController {

    private final WordService service;

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    public WordController(WordService service) {this.service = service;}

    @RequestMapping(value = "**/help", method = RequestMethod.GET)
    public String help() {
        return WordConstants.HELP_MESSAGE;
    }

    @RequestMapping(value = "**/word/{value}", method = RequestMethod.GET)
    public String convertNumberString(@PathVariable String value) throws WordException {
        return service.convertSingle(value);
    }

    @RequestMapping(value = "**/words/{values}", method = RequestMethod.GET)
    public Map<String, String> convertNumberStrings(@PathVariable String[] values) throws WordException {
        return service.convert(values);
    }

    @ExceptionHandler(value
            = {WordException.class})
    protected ResponseEntity<String> handleConflict(
            RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }



}
