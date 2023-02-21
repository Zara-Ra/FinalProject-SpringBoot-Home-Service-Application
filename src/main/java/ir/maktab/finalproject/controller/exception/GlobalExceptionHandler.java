package ir.maktab.finalproject.controller.exception;

import ir.maktab.finalproject.service.exception.*;
import ir.maktab.finalproject.util.exception.ValidationException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> badRequestExceptionHandler(BindException e) {
        for (FieldError fieldError : e.getFieldErrors()) {
            String message = fieldError.getDefaultMessage();
            assert message != null;
            log.error("*** Rejected Value Is : '{}' And Error Is : {} ***",
                    fieldError.getRejectedValue(), e.getLocalizedMessage());
        }
        return badRequestHandler(e);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> badRequestExceptionHandler(ConstraintViolationException e) {
        return badRequestHandler(e);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> badRequestExceptionHandler(IllegalArgumentException e) {
        return badRequestHandler(e);
    }

    @ExceptionHandler(EmailVerificationException.class)
    public ResponseEntity<?> badRequestExceptionHandler(EmailVerificationException e) {
        return badRequestHandler(e);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> badRequestExceptionHandler(ValidationException e) {
        return badRequestHandler(e);
    }

    @ExceptionHandler(NotExistsException.class)
    public ResponseEntity<?> badRequestExceptionHandler(NotExistsException e) {
        return badRequestHandler(e);
    }

    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<?> badRequestExceptionHandler(NotAllowedException e) {
        return badRequestHandler(e);
    }

    @ExceptionHandler(OrderRequirementException.class)
    public ResponseEntity<?> badRequestExceptionHandler(OrderRequirementException e) {
        return badRequestHandler(e);
    }

    @ExceptionHandler(OfferRequirementException.class)
    public ResponseEntity<?> badRequestExceptionHandler(OfferRequirementException e) {
        return badRequestHandler(e);
    }

    private ResponseEntity<?> badRequestHandler(Exception e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        log.error("*** " + e.getLocalizedMessage() + " ***");
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

}
