package project.model.responses.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.enums.LoginStatus;
import project.repository.entity.Transaction;

import javax.persistence.OneToMany;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private String id;
    private String firstName;
    private String surname;
    private String bankName;
    private String phoneNumber;
    private String emailAddress;
    private String customerNumber;
    private String passNumber;
    private String verificationCode;
    private String accountNumber;
    private String sortCode;
    private double balance;
    private String currency;
    @OneToMany
    private List<Transaction> transactions;
    private LoginStatus loginStatus;
}
