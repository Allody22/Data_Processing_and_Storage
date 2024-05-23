package ru.nsu.task4.model.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class NotEnoughMoneyException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
