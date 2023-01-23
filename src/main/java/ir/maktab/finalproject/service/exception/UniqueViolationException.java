package ir.maktab.finalproject.service.exception;

import ir.maktab.finalproject.util.exception.ValidationException;

public class UniqueViolationException extends ValidationException {
    public UniqueViolationException(String message) {
        super(message);
    }
}
