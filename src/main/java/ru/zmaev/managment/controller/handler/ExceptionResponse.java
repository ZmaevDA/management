package ru.zmaev.managment.controller.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    String message;
    String type;
    String path;
    Instant instant;
}
