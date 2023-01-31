package ir.maktab.finalproject.controller.exception;

import ir.maktab.finalproject.service.exception.NotAllowedException;
import ir.maktab.finalproject.service.exception.NotExitsException;
import ir.maktab.finalproject.service.exception.OfferRequirementException;
import ir.maktab.finalproject.service.exception.OrderRequirementException;
import ir.maktab.finalproject.util.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ValidationException.class
            , NotExitsException.class
            , NotAllowedException.class
            , OrderRequirementException.class, OfferRequirementException.class
    })

    public ResponseEntity<?> badRequestExceptionHandler(RuntimeException e) {
        CustomExcpetion exception = new CustomExcpetion(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

}
