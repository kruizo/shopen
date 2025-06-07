package com.bkr.shopen.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

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
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionErrResponse> handleNoHandlerFoundError(NoHandlerFoundException error, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.NOT_FOUND.value(),
            "Endpoint not found",
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionErrResponse> handleGlobalException(Exception error, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred: " + error.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConflictExceptionErr.class)
    public ResponseEntity<?> handleConflict(ConflictExceptionErr ex, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.CONFLICT.value(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // @ExceptionHandler(AccessDeniedException.class)
    // public ResponseEntity<?> handleAccessDenied(Exception ex) {
    //     ExceptionErrResponse response = new ExceptionErrResponse(
    //         HttpStatus.FORBIDDEN.value(),
    //         "Access denied: You do not have permission to access this resource.",
    //         ex.getMessage()
    //     );

    //     return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    // }

    // @ExceptionHandler(AuthenticationException.class)
    // public ResponseEntity<?> handleAuthentication(Exception ex) {
    //     ExceptionErrResponse response = new ExceptionErrResponse(
    //         HttpStatus.UNAUTHORIZED.value(),
    //         "Authentication required.",
    //         ex.getMessage()
    //     );
    //     return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    // }

    @ExceptionHandler(EmailNotVerifiedExceptionErr.class)
    public ResponseEntity<ExceptionErrResponse> handleEmailNotVerifiedError(EmailNotVerifiedExceptionErr error, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.UNAUTHORIZED.value(),
            error.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String param = ex.getName();
        Object valueObj = ex.getValue();
        String value = valueObj != null ? valueObj.toString() : "null";

        Class<?> requiredType = ex.getRequiredType();
        String requiredTypeName = requiredType != null ? requiredType.getSimpleName() : "unknown";
        String message = String.format("Parameter '%s' with value '%s' is not valid. Expected type: %s",
            param, value, requiredTypeName);

        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.BAD_REQUEST.value(),
            message,
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({BadRequestExceptionErr.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleBadRequest(Exception error, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.BAD_REQUEST.value(),
            error.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundExceptionErr.class)
    public ResponseEntity<ExceptionErrResponse> handleNotFoundError(ResourceNotFoundExceptionErr error, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.NOT_FOUND.value(),
            error.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionDeniedExceptionErr.class)
    public ResponseEntity<?> handlePermissionDeniedError(PermissionDeniedExceptionErr error, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.FORBIDDEN.value(),
            error.getMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalServerExceptionErr.class)
    public ResponseEntity<?> handleGlobalException(InternalServerExceptionErr error, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred: " + error.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}