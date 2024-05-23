package ru.nsu.task4.model.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AlreadyCheckedInException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyCheckedInException(String message) {
        super(message);
    }
}
