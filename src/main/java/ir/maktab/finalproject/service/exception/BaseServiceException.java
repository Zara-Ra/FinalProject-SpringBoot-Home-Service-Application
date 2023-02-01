package ir.maktab.finalproject.service.exception;

public class BaseServiceException extends NotExistsException {
    public BaseServiceException(String message) {
        super(message);
    }
}
