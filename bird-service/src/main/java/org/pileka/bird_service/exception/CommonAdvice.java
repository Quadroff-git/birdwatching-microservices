package org.pileka.bird_service.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

// TODO: add logging
@ControllerAdvice
public class CommonAdvice {

    @ExceptionHandler(EntityDoesntExistException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityDoesntExist(EntityDoesntExistException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errorInfo = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();

            errorInfo.put(fieldName, message);
        });

        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, errorInfo.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleGenericBadRequest(RuntimeException e) {
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, "Bad request: " + e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDataAccess(DataAccessException e) {
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while accessing the database:" + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericInternalError(RuntimeException e) {
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error happened: " + e.getMessage());
    }
}
