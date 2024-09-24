package project.model.requests.transaction;

import lombok.Data;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class WithdrawRequest {
    @Pattern(regexp = "^[0-9]{2}-[0-9]{2}-[0-9]{2}$")
    @NotBlank
    private final String sortCode;
    @Pattern(regexp = "^[0-9]{8}$")
    @NotBlank
    private final String accountNumber;
    @DecimalMin(value = "0.0", inclusive = false)
    private final double amount;
}
