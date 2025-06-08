package com.bkr.shopen.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.http.converter.HttpMessageNotReadableException;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErr(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((err) -> {
            String fieldName = ((org.springframework.validation.FieldError) err).getField();
            String errorMessage = err.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionErrResponse> handleGlobalException(Exception error, WebRequest request) {
        error.printStackTrace();
        ExceptionErrResponse response = new ExceptionErrResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred: " + error.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionErrResponse> handleNoHandlerFoundError(NoHandlerFoundException ex, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionErrResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String message = "Invalid request body: " + ex.getMostSpecificCause().getMessage();
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.BAD_REQUEST.value(),
            message,
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionErrResponse> handleMissingParams(Exception error, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Missing required parameter: " + error.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(Exception ex, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.FORBIDDEN.value(),
            "Access denied: You do not have permission to access this resource.",
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthentication(Exception ex, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.UNAUTHORIZED.value(),
                "Bad credentials.",
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConflictExceptionErr.class)
    public ResponseEntity<ExceptionErrResponse> handleConflict(ConflictExceptionErr ex, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.CONFLICT.value(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

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
    public ResponseEntity<ExceptionErrResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
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
    public ResponseEntity<ExceptionErrResponse> handleBadRequest(Exception error, WebRequest request) {
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
    public ResponseEntity<ExceptionErrResponse> handlePermissionDeniedError(PermissionDeniedExceptionErr error, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.FORBIDDEN.value(),
            error.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalServerExceptionErr.class)
    public ResponseEntity<ExceptionErrResponse> handleInternalServerException(InternalServerExceptionErr error, WebRequest request) {
        ExceptionErrResponse response = new ExceptionErrResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred: " + error.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

