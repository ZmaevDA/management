package ru.zmaev.managment.controller.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.zmaev.managment.exception.ClientException;
import org.springframework.beans.TypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final static String VALIDATION = "VALIDATION";

    @ExceptionHandler({ClientException.class})
    public ResponseEntity<ExceptionResponse> handleException(
            ClientException ex,
            ServletWebRequest request
    ) {
        return logAndReturnExceptionResponse(ex, request, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        return logAndReturnValidExceptionResponse(ex, request);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(
            TypeMismatchException ex,
            WebRequest request
    ) {
        return logAndReturnValidExceptionResponse(ex, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request
    ) {
        return logAndReturnValidExceptionResponse(ex, (ServletWebRequest) request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {
        return logAndReturnValidExceptionResponse(ex, (ServletWebRequest) request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            WebRequest request
    ) {
        return logAndReturnValidExceptionResponse(ex, (ServletWebRequest) request);
    }

    private ResponseEntity<ExceptionResponse> logAndReturnExceptionResponse(
            ClientException ex,
            ServletWebRequest request,
            HttpStatus status
    ) {
        log.warn("<{}> status with {}", status.value(), ex.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                ex.getName(),
                request.getRequest().getRequestURI(),
                Instant.now());
        return ResponseEntity.status(status).body(exceptionResponse);
    }

    private ResponseEntity<Object> logAndReturnValidExceptionResponse(
            TypeMismatchException ex,
            WebRequest request
    ) {
        logDetails(ex.getStackTrace());
        return new ResponseEntity<>(
                new ExceptionResponse(
                        "Request Body not valid: field " + ex.getPropertyName(),
                        VALIDATION,
                        ((ServletWebRequest) request).getRequest().getRequestURI(),
                        Instant.now()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    private ResponseEntity<Object> logAndReturnValidExceptionResponse(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        logDetails(ex.getStackTrace());
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .toList()
                .toString();

        return new ResponseEntity<>(
                new ExceptionResponse(
                        errorMessage,
                        VALIDATION,
                        ((ServletWebRequest) request).getRequest().getRequestURI(),
                        Instant.now()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    private static ResponseEntity<ExceptionResponse> logAndReturnValidExceptionResponse(
            IllegalArgumentException ex,
            ServletWebRequest request) {
        logDetails(ex.getStackTrace());
        return new ResponseEntity<>(
                new ExceptionResponse(
                        "Invalid input: " + ex.getMessage(),
                        "VALIDATION",
                        request.getRequest().getRequestURI(),
                        Instant.now()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    private static ResponseEntity<Object> logAndReturnValidExceptionResponse(
            ConstraintViolationException ex,
            ServletWebRequest request) {
        logDetails(ex.getStackTrace());
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                new ExceptionResponse(
                        errorMessage,
                        VALIDATION,
                        request.getRequest().getRequestURI(),
                        Instant.now()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    private static ResponseEntity<Object> logAndReturnValidExceptionResponse(
            HttpMessageNotReadableException ex,
            ServletWebRequest request) {
        logDetails(ex.getStackTrace());

        return new ResponseEntity<>(new ExceptionResponse(
                ex.getMessage(),
                VALIDATION,
                request.getRequest().getRequestURI(),
                Instant.now()
        ), HttpStatus.BAD_REQUEST);
    }

    private static void logDetails(StackTraceElement[] ex) {
        log.warn("<{}> status in {}", HttpStatus.BAD_REQUEST.value(), ex);
    }
}
