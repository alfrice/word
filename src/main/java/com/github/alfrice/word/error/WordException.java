package com.github.alfrice.word.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by alice.martin
 * Developer: alice.martin
 * Date: 12/8/18
 * Time: 1:39 PM
 * Description: com.github.alfrice.Word.error
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WordException extends RuntimeException {
    public WordException(String s, Throwable t) {
        super(s, t);
    }

    public WordException(String s) {
        super(s);
    }
}
