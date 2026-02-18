package com.practice.tmdb.api;

import com.practice.tmdb.exception.InvalidDataException;
import com.practice.tmdb.exception.NotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    @Getter
    static class Error {
        private final String reason;
        private final String message;
        Error(String reason, String message) {
            this.reason = reason;
            this.message = message;
        }
    }
    // 400 Error
    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleInvalidDataException(InvalidDataException ex) {
        log.warn("Invalid data: {}", ex.getMessage());
        return new Error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());
    }

    // 404 Error
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(NotFoundException ex) {
        log.warn("Not found: {}", ex.getMessage());
        return new Error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());
    }

    // 500 Error
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleUnknownException(Exception ex) {
        log.error("Unexpected error", ex);
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Something went wrong");
    }
}