package project.model.requests.account;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreateAccountRequest {
    @Pattern(regexp = "^[a-z A-Z]+$")
    @NotBlank
    private final String bankName;
    @Pattern(regexp = "^[a-z A-Z]+$")
    @NotBlank
    private final String firstName;
    @Pattern(regexp = "^[a-z A-Z]+$")
    @NotBlank
    private final String surname;
    @Pattern(regexp = "^\\+(?:\\d{1,3})?\\d{9,15}$")
    @NotBlank
    private final String phoneNumber;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @NotBlank
    private final String emailAddress;
    @Pattern(regexp = "^[a-z A-Z]+$")
    @NotBlank
    private final String currency;
}
