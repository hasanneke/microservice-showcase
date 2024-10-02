package com.mentoapp.mentoauth.Exception;

import com.mentoapp.mentoauth.Exception.Instances.ResourceAlreadyExistException;
import com.mentoapp.mentoauth.Exception.Instances.TokenExpired;
import com.mentoapp.mentoauth.Exception.Instances.UserNotFoundException;
import com.mentoapp.mentoauth.Utility.ResponseError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ResponseError> handleUserAlreadyExistException(ResourceAlreadyExistException ex) {
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("already_exist").
                message("User already exist").build(),
                HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(ResponseError.builder()
                .errorCode("bad_credentials")
                .message("Bad Credentials")
                .timeStamp(System.currentTimeMillis())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpired.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(TokenExpired ex) {
        return new ResponseEntity<>(ResponseError.builder()
                .errorCode("token_expired")
                .message("Token Expired")
                .timeStamp(System.currentTimeMillis())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseError> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("user_not_found").
                message("User not found").build(),
                HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseError> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("access_denied").
                message("Access to this resource is denied, check the credentials").build(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ResponseError> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("method_not_allowed").
                message(String.join(", ", Objects.requireNonNull(ex.getSupportedHttpMethods()).stream().map(HttpMethod::name).toList())).build(),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseError> handleGenericException(Exception ex) {
        System.out.println(ex);
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("internal_server_error").
                message("Internal Error").build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseError> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        System.out.println(ex);
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("internal_server_error").
                message("Internal Error").build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseError> handleAuthenticationExceptionHandler(AuthenticationException ex) {
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("auth_exception").
                message(ex.getMessage()).build(),
                HttpStatus.UNAUTHORIZED);
    }
}
