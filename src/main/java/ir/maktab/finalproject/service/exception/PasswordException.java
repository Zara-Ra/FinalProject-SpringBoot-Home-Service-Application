package ir.maktab.finalproject.service.exception;

import ir.maktab.finalproject.util.exception.ValidationException;

public class PasswordException extends ValidationException {
    public PasswordException(String message) {
        super(message);
    }
}
