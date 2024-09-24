package project.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LoginStatus {

    LOGIN_ACCESS_FAILED(false),
    LOGIN_ACCESS_GRANTED(true);

    private boolean status;
    LoginStatus(boolean status) {
        this.status = status;
    }

    @JsonValue
    public boolean getStatus() {
        return status;
    }
}
