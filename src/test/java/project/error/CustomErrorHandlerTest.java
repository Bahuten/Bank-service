package project.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.errorhandler.CustomErrorHandler;
import project.errorhandler.ErrorResponse;
import project.exception.AccountNotFoundException;
import project.exception.AccountExistsException;
import project.exception.AccountVerificationFailureException;
import project.exception.TransactionNotFoundException;
import project.exception.InvalidRequestException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomErrorHandlerTest {

    private final CustomErrorHandler customErrorHandler = new CustomErrorHandler();

    @Test
    public void invalidRequestExceptionTest() {
        ResponseEntity<Object> response = customErrorHandler.invalidRequestException(new InvalidRequestException("Test"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request could not be processed", ((ErrorResponse) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals("Test", ((ErrorResponse) response.getBody()).getProperties().get("cause"));
    }

    @Test
    public void accountNotFoundExceptionTest() {
        ResponseEntity<Object> response = customErrorHandler.accountNotFoundException(new AccountNotFoundException("Test"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Account not found", ((ErrorResponse) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals("Test", ((ErrorResponse) response.getBody()).getProperties().get("cause"));
    }

    @Test
    public void accountExistsExceptionTest() {
        ResponseEntity<Object> response = customErrorHandler.accountExistsException(new AccountExistsException("Test"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request could not be processed", ((ErrorResponse) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals("Test", ((ErrorResponse) response.getBody()).getProperties().get("cause"));
    }

    @Test
    public void transactionNotFoundExceptionTest() {
        ResponseEntity<Object> response = customErrorHandler.transactionNotFoundException(new TransactionNotFoundException("Test"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Transaction not found", ((ErrorResponse) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals("Test", ((ErrorResponse) response.getBody()).getProperties().get("cause"));
    }

    @Test
    public void accountVerificationFailureExceptionTest() {
        ResponseEntity<Object> response = customErrorHandler.accountVerificationFailureException(new AccountVerificationFailureException("Test"));
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Account not verified", ((ErrorResponse) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals("Test", ((ErrorResponse) response.getBody()).getProperties().get("cause"));
    }
}
