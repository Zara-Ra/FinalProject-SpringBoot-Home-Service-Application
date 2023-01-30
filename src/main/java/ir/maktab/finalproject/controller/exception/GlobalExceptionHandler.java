package ir.maktab.finalproject.controller.exception;

import ir.maktab.finalproject.service.exception.*;
import ir.maktab.finalproject.util.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BaseServiceException.class, SubServiceException.class
            , UniqueViolationException.class, UpdatableViolationException.class, ValidationException.class
            , UserNotFoundException.class, PasswordException.class
            , OrderRequirementException.class, OfferRequirementException.class})

    public ResponseEntity<?> userNameDuplicateExceptionHandler(BaseServiceException e) {
        CustomExcpetion exception = new CustomExcpetion(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

}
