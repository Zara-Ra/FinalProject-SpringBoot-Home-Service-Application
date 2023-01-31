package ir.maktab.finalproject.service.exception;

public class UserNotFoundException extends NotExitsException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
