package ir.maktab.finalproject.controller.exception;

import ir.maktab.finalproject.service.exception.NotAllowedException;
import ir.maktab.finalproject.service.exception.NotExistsException;
import ir.maktab.finalproject.service.exception.OfferRequirementException;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import ir.maktab.finalproject.util.exception.ValidationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> badRequestExceptionHandler(MethodArgumentNotValidException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getDetailMessageArguments()[1].toString());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> badRequestExceptionHandler(ConstraintViolationException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> badRequestExceptionHandler(ValidationException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(NotExistsException.class)
    public ResponseEntity<?> badRequestExceptionHandler(NotExistsException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<?> badRequestExceptionHandler(NotAllowedException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(OrderRequirementException.class)
    public ResponseEntity<?> badRequestExceptionHandler(OrderRequirementException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(OfferRequirementException.class)
    public ResponseEntity<?> badRequestExceptionHandler(OfferRequirementException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

}
