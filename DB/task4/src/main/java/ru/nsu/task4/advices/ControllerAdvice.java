package ru.nsu.task4.advices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nsu.task4.model.exceptions.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    private final MessageSource messageSource;

    @Autowired
    ControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(LanguageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleLanguageValidationException(LanguageNotFoundException ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("message", errorMessage);
        return errors;
    }

    @ExceptionHandler(NoSeatsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleNoSeatsException(NoSeatsException ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("message", errorMessage);
        return errors;
    }

    @ExceptionHandler(AlreadyCheckedInException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyCheckedInException(AlreadyCheckedInException ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("message", errorMessage);
        return errors;
    }


    @ExceptionHandler(RaceIsGoneException.class)
    @ResponseStatus(HttpStatus.GONE)
    public Map<String, String> handleRaceIsGoneException(RaceIsGoneException ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("message", errorMessage);
        return errors;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundInDataBaseException(NotFoundException ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("message", errorMessage);
        return errors;
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleNotEnoughMoneyExceptionException(NotEnoughMoneyException ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("message", errorMessage);
        return errors;
    }
}
