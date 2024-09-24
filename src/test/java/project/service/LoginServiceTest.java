package project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import project.enums.LoginStatus;
import project.exception.AccountNotFoundException;
import project.repository.AccountRepository;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.service.impl.LoginServiceImpl;
import project.utility.CodeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoginServiceTest {
    @Mock
    Account currentAccount;
    String sourceAccountId;
    String targetAccountId;
    String name;
    String surname;
    String bankName;
    String phoneNumber;
    String emailAddress;
    String customerNumber;
    String passNumber;
    String verificationCode;
    String currency;
    String accountNumber;
    String sortCode;
    double balance;
    List<Transaction> transactions;
    LoginStatus loginStatusOn;
    LoginStatus loginStatusOff;
    private LoginServiceImpl loginService;
    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        sourceAccountId = UUID.randomUUID().toString();
        targetAccountId = UUID.randomUUID().toString();
        name = "Martin";
        surname = "King";
        bankName = "NatWest";
        phoneNumber = "+447587155942";
        emailAddress = "william.cuthbert@fisglobal.com";
        customerNumber = CodeGenerator.generateCustomerNumber();
        passNumber = CodeGenerator.generatePassNumber();
        verificationCode = CodeGenerator.generateVerificationCode();
        currency = "GBP";
        accountNumber = CodeGenerator.generateAccountNumber();
        sortCode = CodeGenerator.generateSortCode();
        balance = 0;
        transactions = new ArrayList<>();
        loginStatusOn = LoginStatus.LOGIN_ACCESS_GRANTED;
        loginStatusOff = LoginStatus.LOGIN_ACCESS_FAILED;
        currentAccount = new Account(sourceAccountId, name, surname, bankName, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), loginStatusOn);
        loginService = new LoginServiceImpl(accountRepository);
    }

    @Test
    public void openSession() {
        String customerNumber = currentAccount.getCustomerNumber();
        String passNumber = currentAccount.getPassNumber();

        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(currentAccount));
        assertEquals(currentAccount, loginService.openSession(customerNumber, passNumber));
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
    }

    @Test
    public void openSessionFailed() {
        String customerNumber = currentAccount.getCustomerNumber();
        String passNumber = currentAccount.getPassNumber();

        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> loginService.openSession(customerNumber, passNumber));
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
    }

    @Test
    public void closeSession() {
        String id = currentAccount.getId();

        when(accountRepository.findById(id)).thenReturn(Optional.of(currentAccount));
        assertEquals(currentAccount, loginService.closeSession(id));
        assertEquals(loginStatusOff.getStatus(), currentAccount.getLoginStatus().getStatus());
        verify(accountRepository).findById(id);
    }

    @Test
    public void closeSessionFailed() {
        String id = currentAccount.getId();

        when(accountRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> loginService.closeSession(id));
        verify(accountRepository).findById(id);
    }

    @Test
    public void isLoggedOn() {
        String customerNumber = currentAccount.getCustomerNumber();
        String passNumber = currentAccount.getPassNumber();

        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(currentAccount));
        assertEquals(loginStatusOn.getStatus(), currentAccount.getLoginStatus().getStatus());
        assertEquals(loginStatusOn.getStatus(), loginService.isLoggedOn(customerNumber, passNumber));
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
    }

    @Test
    public void isLoggedOff() {
        String customerNumber = currentAccount.getCustomerNumber();
        String passNumber = currentAccount.getPassNumber();

        currentAccount.setLoginStatus(LoginStatus.LOGIN_ACCESS_FAILED);

        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(currentAccount));
        assertEquals(loginStatusOff.getStatus(), currentAccount.getLoginStatus().getStatus());
        assertEquals(loginStatusOff.getStatus(), loginService.isLoggedOn(customerNumber, passNumber));
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
    }

    @Test
    public void isLoggedOnFailed() {
        String customerNumber = currentAccount.getCustomerNumber();
        String passNumber = currentAccount.getPassNumber();

        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> loginService.isLoggedOn(customerNumber, passNumber));
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
    }

    @Test
    public void verifyLogin() {
        String id = currentAccount.getId();
        String verificationCode = currentAccount.getVerificationCode();
        boolean verified = true;

        when(accountRepository.findById(id)).thenReturn(Optional.of(currentAccount));
        assertEquals(verified, loginService.verifyLogin(id, verificationCode));
        verify(accountRepository).findById(id);
    }

    @Test
    public void notVerifiedLogin() {
        String id = currentAccount.getId();
        String verificationCode = CodeGenerator.generateVerificationCode();
        boolean verified = false;

        when(accountRepository.findById(id)).thenReturn(Optional.of(currentAccount));
        assertEquals(verified, loginService.verifyLogin(id, verificationCode));
        verify(accountRepository).findById(id);
    }
}
