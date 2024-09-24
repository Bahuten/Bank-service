package project.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.enums.LoginStatus;
import project.model.requests.account.UpdateAccountRequest;
import project.model.responses.account.AccountsResponse;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.model.responses.account.AccountResponse;
import project.utility.CodeGenerator;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountConverterTest {

    List<Account> accountList;
    private Account account;
    private AccountResponse accountResponse;
    private AccountsResponse accountsResponse;
    private UpdateAccountRequest updateAccountRequest;

    // todo: create account request

    @BeforeEach
    public void setup() {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String surname = "King";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String customerNumber = CodeGenerator.generateCustomerNumber();
        String passNumber = CodeGenerator.generatePassNumber();
        String verificationCode = CodeGenerator.generateVerificationCode();
        String currency = "GBP";
        String accountNo = CodeGenerator.generateAccountNumber();
        String sortCode = CodeGenerator.generateSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;
        accountList = new ArrayList<>();
        account = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber,
                passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);
        accountResponse = new AccountResponse(id, name, surname, bank, phoneNumber, emailAddress, customerNumber,
                passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);
        accountsResponse = new AccountsResponse(accountList);
        updateAccountRequest = new UpdateAccountRequest(phoneNumber, emailAddress);
    }

    @Test
    void covertAccountToAccountResponse() {
        assertEquals(accountResponse, AccountConverter.convertToAccountResponse(account));
    }

    @Test
    void convertAccountsToAccountsResponse() {
        assertEquals(accountsResponse, AccountConverter.convertAccountsToAccountsResponse(accountList));
    }

    @Test
    void covertAccountResponseToAccount() {
        assertEquals(account, AccountConverter.covertAccountResponseToAccount(accountResponse));
    }

    @Test
    void convertAccountRequestToAccount() {
        assertEquals(account, AccountConverter.convertAccountRequestToAccount(account.getId(), updateAccountRequest, account));
    }
}
