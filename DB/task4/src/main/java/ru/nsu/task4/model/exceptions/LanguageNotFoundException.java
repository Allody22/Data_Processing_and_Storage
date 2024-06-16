package ru.nsu.task4.model.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LanguageNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public LanguageNotFoundException(String message) {
        super(message);
    }
}
