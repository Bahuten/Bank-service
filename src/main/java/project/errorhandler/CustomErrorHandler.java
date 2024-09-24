package project.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.exception.*;

@Slf4j
@ControllerAdvice
public class CustomErrorHandler {
    @ExceptionHandler(value = {InvalidRequestException.class})
    public ResponseEntity<Object> invalidRequestException(InvalidRequestException ex) {
        log.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "badRequest");
        errorResponse.getProperties().put("cause", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.badRequest(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AccountNotFoundException.class})
    public ResponseEntity<Object> accountNotFoundException(AccountNotFoundException ex) {
        log.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "notFound");
        errorResponse.getProperties().put("cause", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.accountNotFound(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AccountExistsException.class})
    public ResponseEntity<Object> accountExistsException(AccountExistsException ex) {
        log.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "badRequest");
        errorResponse.getProperties().put("cause", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.badRequest(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {TransactionNotFoundException.class})
    public ResponseEntity<Object> transactionNotFoundException(TransactionNotFoundException ex) {
        log.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "notFound");
        errorResponse.getProperties().put("cause", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.transactionNotFound(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AccountVerificationFailureException.class})
    public ResponseEntity<Object> accountVerificationFailureException(AccountVerificationFailureException ex) {
        log.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "Forbidden");
        errorResponse.getProperties().put("cause", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.accountVerificationFailure(ex.getMessage()), HttpStatus.FORBIDDEN);
    }
}
