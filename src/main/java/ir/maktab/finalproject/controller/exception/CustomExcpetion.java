package ir.maktab.finalproject.controller.exception;

import org.springframework.http.HttpStatus;

public record CustomExcpetion(HttpStatus httpStatus, String message) {
}
