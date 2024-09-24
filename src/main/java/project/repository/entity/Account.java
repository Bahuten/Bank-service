package project.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.enums.LoginStatus;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
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