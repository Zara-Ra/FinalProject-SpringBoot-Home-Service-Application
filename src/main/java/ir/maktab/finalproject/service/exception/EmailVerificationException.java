package ir.maktab.finalproject.service.exception;

public class EmailVerificationException extends RuntimeException {
    public EmailVerificationException(String message) {
        super(message);
    }
}
