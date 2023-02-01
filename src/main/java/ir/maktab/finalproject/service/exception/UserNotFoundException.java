package ir.maktab.finalproject.service.exception;

public class UserNotFoundException extends NotExistsException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
