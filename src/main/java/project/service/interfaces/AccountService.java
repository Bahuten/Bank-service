package project.service.interfaces;

import project.repository.entity.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(String id, String bankName, String firstName, String surname, String phoneNumber, String emailAddress, String customerNumber, String passNumber, String verificationCode, String currency, String sortCode, String accountNo);

    Account getAccountBySortCodeAndAccountNumber(String sortCode, String accountNumber);

    Account getAccountByNumber(String accountNumber);

    Account getAccountById(String id);

    List<Account> getAccounts();

    Account updateAccount(Account account);

    void deleteAccount(String id);
}
