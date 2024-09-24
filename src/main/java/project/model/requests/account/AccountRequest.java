package project.model.requests.account;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class AccountRequest {
    @Pattern(regexp = "^[0-9]{2}-[0-9]{2}-[0-9]{2}$")
    @NotBlank
    private final String sortCode;
    @Pattern(regexp = "^[0-9]{8}$")
    @NotBlank
    private final String accountNumber;
}
