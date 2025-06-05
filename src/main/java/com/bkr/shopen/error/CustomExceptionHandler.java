package com.bkr.shopen.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class CustomExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErr(MethodArgumentNotValidException ex) {
        
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((err) -> {

            String fieldName = ((org.springframework.validation.FieldError) err).getField();
            String errorMessage = err.getDefaultMessage();
            errors.put(fieldName, errorMessage);

        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundExceptionErr.class)
    public ResponseEntity<?> handleErrorResponse(ResourceNotFoundExceptionErr error, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("message", error.getMessage());
        body.put("path", request.getDescription(false));
        body.put("timestamp", LocalDateTime.now());
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionDeniedExceptionErr.class)
    public ResponseEntity<?> handlePermissionDeniedError(PermissionDeniedExceptionErr error, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("message", error.getMessage());
        body.put("path", request.getDescription(false));
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalServerExceptionErr.class)
    public ResponseEntity<?> handleGlobalException(InternalServerExceptionErr error, WebRequest request) {
                Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("message", error.getMessage());
        body.put("path", request.getDescription(false));
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BadRequestExceptionErr.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleBadRequest(Exception error, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", error.getMessage());
        body.put("path", request.getDescription(false));
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }



    

}