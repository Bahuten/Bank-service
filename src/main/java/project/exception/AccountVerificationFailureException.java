package project.exception;

public class AccountVerificationFailureException extends RuntimeException {
    public AccountVerificationFailureException(String message) {
        super(message);
    }
}
