package ir.maktab.finalproject.controller.exception;

import org.springframework.http.HttpStatus;

public record CustomException(HttpStatus httpStatus, String message) {
}
