package ru.zmaev.managment.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends ClientException {

    public ConflictException(String name, String message) {
        super(HttpStatus.CONFLICT, name, message);
    }
}
