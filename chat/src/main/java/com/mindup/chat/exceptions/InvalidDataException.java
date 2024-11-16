package com.mindup.chat.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidDataException extends RuntimeException {
    private String message;
}
