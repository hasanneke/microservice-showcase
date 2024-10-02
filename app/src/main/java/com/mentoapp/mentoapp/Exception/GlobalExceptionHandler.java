package com.mentoapp.mentoapp.Exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mentoapp.mentoapp.Exception.Instance.*;
import com.mentoapp.mentoapp.Utility.ResponseError;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(BadCredentialsException ex) {
        logger.error("Bad Credentials Exception", ex);
        return new ResponseEntity<>(ResponseError.builder()
                .errorCode("bad_credentials")
                .message("Bad Credentials")
                .timeStamp(System.currentTimeMillis())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ResponseError> handleUserAlreadyExistException(ResourceAlreadyExistException ex) {
        logger.error("User Already Exist Exception", ex);
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("already_exist").
                message("User already exist").build(),
                HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceAlreadyUpdatedException.class)
    public ResponseEntity<ResponseError> handleResourceUpdateException(ResourceAlreadyUpdatedException ex) {
        logger.error("Resource Already Updated Exception", ex);
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("already_updated").
                message(ex.getMessage()).build(),
                HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UpdateFailedException.class)
    public ResponseEntity<ResponseError> handleUpdateFailedException(UpdateFailedException ex) {
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("update_failed").
                message(ex.getMessage()).build(),
                HttpStatus.CONFLICT);
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ResponseError> handleResourceNotFoundException(ResourceNotFound ex) {
        logger.error("Resource Not Found Exception", ex);
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("resource_not_found").
                message(ex.getMessage()).build(),
                HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        logger.error("Constraint Violation Exception", ex);
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
        logger.error("Method Argument Not Valid Exception", ex);
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
        logger.error("Access Denied Exception", ex);
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
        logger.error("Method Not Allowed Exception", ex);
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("method_not_allowed").
                message(String.join(", ", Objects.requireNonNull(ex.getSupportedHttpMethods()).stream().map(HttpMethod::name).toList())).build(),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseError> handleGenericException(NoResourceFoundException ex) {
        logger.error("No Resource Found Exception", ex);
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("page_not_found").
                message("Page not found").build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseError> handleGenericException(Exception ex) {
        logger.error("Exception", ex);
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
        logger.error("Unsupported Operation Exception", ex);
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

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseError> handleAuthenticationExceptionHandler(ExpiredJwtException ex) {
        logger.error("JWT Expired Exception", ex);
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("jwt_expired").
                message(ex.getMessage()).build(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseError> handleJsonProcessingException(JsonProcessingException ex) {
        logger.error("Json Processing Exception", ex);
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("internal_error").
                message(ex.getMessage()).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
