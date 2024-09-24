package project.model.requests.transaction;

import lombok.Data;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class TransferRequest {
    @DecimalMin(value = "0.00", inclusive = false)
    private final double amount;
    @Pattern(regexp = "^[a-z A-Z]+$")
    @NotBlank
    private final String reference;
}
