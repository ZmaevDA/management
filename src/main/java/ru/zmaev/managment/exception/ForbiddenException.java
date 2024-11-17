package ru.zmaev.managment.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ClientException {
    public ForbiddenException(String name, String message) {
        super(HttpStatus.FORBIDDEN, name, message);
    }
}
