package project.model.responses.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.enums.Payment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String id;
    private String sourceAccountId;
    private String targetAccountId;
    private String firstName;
    private String surname;
    private double amount;
    private String initiationDate;
    private String completionDate;
    private String reference;
    private Payment paymentType;
}
