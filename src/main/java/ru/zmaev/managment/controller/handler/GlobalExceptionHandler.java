package ru.zmaev.managment.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import ru.zmaev.managment.exception.ClientException;

import java.time.Instant;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ClientException.class})
    public ResponseEntity<ExceptionResponse> handleException(
            ClientException ex,
            ServletWebRequest request
    ) {
        logAndReturnExceptionResponse(ex, request, ex.getStatus());
        return logAndReturnExceptionResponse(ex, request, HttpStatus.NOT_FOUND);
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
}
