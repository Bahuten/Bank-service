package project.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Payment {
    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),
    TRANSFER("Transfer"),
    REFUND("Refund");

    private final String value;

    Payment(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}