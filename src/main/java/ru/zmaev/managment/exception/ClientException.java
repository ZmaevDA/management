package ru.zmaev.managment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ClientException extends RuntimeException {
    private final HttpStatus status;
    private final String name;
    private final String message;

    public ClientException(HttpStatus status, String name, String message) {
        this.status = status;
        this.name = name;
        this.message = message;
    }
}
