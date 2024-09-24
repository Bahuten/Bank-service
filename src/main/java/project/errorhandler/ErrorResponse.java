package project.errorhandler;

import project.enums.ErrorMessage;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {
    private final Map<String, Object> properties = new HashMap<>();
    private static final String ERROR_NAME_KEY = "errorName";
    private static final String MESSAGE_KEY = "message";
    private static final String CAUSE_KEY =  "cause";

    public ErrorResponse(String message, String errorName) {
        this.properties.put(MESSAGE_KEY, message);
        this.properties.put(ERROR_NAME_KEY, errorName);
    }

    public String getMessage() {
        return properties.get(MESSAGE_KEY).toString();
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public static ErrorResponse badRequest(String cause) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorMessage.BAD_REQUEST.getMessage(), ErrorMessage.BAD_REQUEST.getErrorName());
        errorResponse.getProperties().put(CAUSE_KEY, cause);
        return errorResponse;
    }

    public static ErrorResponse accountNotFound(String cause) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorMessage.ACCOUNT_NOT_FOUND.getMessage(), ErrorMessage.ACCOUNT_NOT_FOUND.getErrorName());
        errorResponse.getProperties().put(CAUSE_KEY, cause);
        return errorResponse;
    }

    public static ErrorResponse transactionNotFound(String cause) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorMessage.TRANSACTION_NOT_FOUND.getMessage(), ErrorMessage.TRANSACTION_NOT_FOUND.getErrorName());
        errorResponse.getProperties().put(CAUSE_KEY, cause);
        return errorResponse;
    }

    public static ErrorResponse accountVerificationFailure(String cause) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorMessage.ACCOUNT_VERIFICATION_FAILURE.getMessage(), ErrorMessage.ACCOUNT_VERIFICATION_FAILURE.getErrorName());
        errorResponse.getProperties().put(CAUSE_KEY, cause);
        return errorResponse;
    }
}
