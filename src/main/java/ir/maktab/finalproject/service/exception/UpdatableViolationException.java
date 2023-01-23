package ir.maktab.finalproject.service.exception;

import ir.maktab.finalproject.util.exception.ValidationException;

public class UpdatableViolationException extends ValidationException {
    public UpdatableViolationException(String message) {
        super(message);
    }
}
