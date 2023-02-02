package ir.maktab.finalproject.controller.exception;

import ir.maktab.finalproject.service.exception.NotAllowedException;
import ir.maktab.finalproject.service.exception.NotExistsException;
import ir.maktab.finalproject.service.exception.OfferRequirementException;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import ir.maktab.finalproject.util.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)

    public ResponseEntity<?> badRequestExceptionHandler(ValidationException e) {
        CustomExcpetion exception = new CustomExcpetion(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    public ResponseEntity<?> badRequestExceptionHandler(NotExistsException e) {
        CustomExcpetion exception = new CustomExcpetion(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    public ResponseEntity<?> badRequestExceptionHandler(NotAllowedException e) {
        CustomExcpetion exception = new CustomExcpetion(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    public ResponseEntity<?> badRequestExceptionHandler(OrderRequirementException e) {
        CustomExcpetion exception = new CustomExcpetion(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    public ResponseEntity<?> badRequestExceptionHandler(OfferRequirementException e) {
        CustomExcpetion exception = new CustomExcpetion(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

}
