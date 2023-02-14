package ir.maktab.finalproject.service.exception;

import ir.maktab.finalproject.util.exception.ValidationException;

public class CreditException extends ValidationException {
    public CreditException(String message) {
        super(message);
    }
}
