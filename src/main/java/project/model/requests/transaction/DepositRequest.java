package project.model.requests.transaction;

import lombok.Data;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class DepositRequest {
    @Pattern(regexp = "^[0-9]{8}$")
    @NotBlank
    private final String accountNumber;
    @DecimalMin(value = "0.00")
    private final double amount;
}
