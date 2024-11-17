package ru.zmaev.managment.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ClientException {
    public NotFoundException(String name, String message) {
        super(HttpStatus.NOT_FOUND, name, message);
    }
}
