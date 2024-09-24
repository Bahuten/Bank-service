package project.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class VerifyRequest {
    @Pattern(regexp = "^[0-9]{2}-[0-9]{2}-[0-9]{2}$")
    @NotBlank
    private String id;
    @Pattern(regexp = "[0-9]{4}")
    @NotBlank
    private String verificationCode;
}
