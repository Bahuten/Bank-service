package project.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class LoginRequest {
    @Pattern(regexp = "[0-9]{11}")
    @NotBlank
    private String customerNumber;
    @Pattern(regexp = "[0-9]{6}")
    @NotBlank
    private String passNumber;
}
