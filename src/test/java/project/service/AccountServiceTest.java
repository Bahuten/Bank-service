package project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import project.enums.LoginStatus;
import project.exception.AccountExistsException;
import project.exception.AccountNotFoundException;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.repository.AccountRepository;
import project.repository.TransactionRepository;
import project.service.impl.AccountServiceImpl;
import project.utility.CodeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountServiceImpl accountServiceImpl;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Test
    public void createAccount() {
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
        Account accountExpected = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);

        when(accountRepository.findBySortCodeAndAccountNumber(sortCode, accountNo)).thenReturn(Optional.empty());
        when(accountRepository.save(accountExpected)).thenReturn(accountExpected);
        Account accountActual = accountServiceImpl.createAccount(id, bank, name, surname, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, currency, sortCode, accountNo);
        verify(accountRepository).findBySortCodeAndAccountNumber(sortCode, accountNo);
        verify(accountRepository).save(accountExpected);
        assertEquals(accountExpected, accountActual);
    }

    @Test
    public void createAccountWithRepeatedSortCodeAndAccountNumber() {
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
        String accountNumber = CodeGenerator.generateAccountNumber();
        String sortCode = CodeGenerator.generateSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;
        Account accountExpected = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, transactions, loginStatus);

        when(accountRepository.findBySortCodeAndAccountNumber(sortCode, accountNumber)).thenReturn(Optional.of(accountExpected));
        assertThrows(AccountExistsException.class,
                () -> accountServiceImpl.createAccount(id, bank, name, surname, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, currency, sortCode, accountNumber));
        verify(accountRepository).findBySortCodeAndAccountNumber(sortCode, accountNumber);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    public void getAccountById() {
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
        Account accountExpected = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);

        when(accountRepository.findById(id)).thenReturn(Optional.of(accountExpected));
        Account accountActual = accountServiceImpl.getAccountById(id);
        verify(accountRepository).findById(id);
        assertEquals(accountExpected, accountActual);
    }

    @Test
    public void getAccountByIdMissing() {
        String id = UUID.randomUUID().toString();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountServiceImpl.getAccountById(id));
        verify(accountRepository).findById(id);
    }

    @Test
    public void getAccountBySortCodeAndAccountNumber() {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String surname = "King";
        String bankName = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String customerNumber = CodeGenerator.generateCustomerNumber();
        String passNumber = CodeGenerator.generatePassNumber();
        String verificationCode = CodeGenerator.generateVerificationCode();
        String currency = "GBP";
        String accountNumber = CodeGenerator.generateAccountNumber();
        String sortCode = CodeGenerator.generateSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;
        Account accountExpected = new Account(id, name, surname, bankName, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, transactions, loginStatus);

        when(accountRepository.findBySortCodeAndAccountNumber(sortCode, accountNumber)).thenReturn(Optional.of(accountExpected));
        Account accountActual = accountServiceImpl.getAccountBySortCodeAndAccountNumber(sortCode, accountNumber);
        verify(accountRepository).findBySortCodeAndAccountNumber(sortCode, accountNumber);
        assertEquals(accountExpected, accountActual);
    }

    @Test
    public void getAccountBySortCodeAndAccountNumberMissing() {
        String sortCode = CodeGenerator.generateSortCode();
        String accountNumber = CodeGenerator.generateAccountNumber();
        when(accountRepository.findBySortCodeAndAccountNumber(sortCode, accountNumber)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountServiceImpl.getAccountBySortCodeAndAccountNumber(sortCode, accountNumber));
        verify(accountRepository).findBySortCodeAndAccountNumber(sortCode, accountNumber);
    }

    @Test
    public void getAccountByAccountNumber() {
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
        Account accountExpected = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);

        when(accountRepository.findAccountByAccountNumber(accountNo)).thenReturn(Optional.of(accountExpected));
        Account accountActual = accountServiceImpl.getAccountByNumber(accountNo);
        verify(accountRepository).findAccountByAccountNumber(accountNo);
        assertEquals(accountExpected, accountActual);
    }

    @Test
    public void getAccountByAccountNumberMissing() {
        String accountNo = CodeGenerator.generateAccountNumber();
        when(accountRepository.findAccountByAccountNumber(accountNo)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountServiceImpl.getAccountByNumber(accountNo));
        verify(accountRepository).findAccountByAccountNumber(accountNo);
    }

    @Test
    public void getAccounts() {
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

        List<Account> accountsExpected = new ArrayList<>();
        accountsExpected.add(new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus));
        accountsExpected.add(new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus));

        when(accountRepository.findAll()).thenReturn(accountsExpected);
        List<Account> accountsActual = accountServiceImpl.getAccounts();
        verify(accountRepository).findAll();
        assertEquals(accountsExpected, accountsActual);
    }

    @Test
    public void deleteAccount() {
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
        Account account = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(account));
        accountServiceImpl.deleteAccount(id);
        verify(accountRepository).deleteById(id);
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
    }

    @Test
    public void deleteAccountDoesNotExist() {
        String id = UUID.randomUUID().toString();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountServiceImpl.deleteAccount(id));
    }

    @Test
    public void updateAccount() {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String surname = "King";
        String bank = "NatWest";
        String customerNumber = CodeGenerator.generateCustomerNumber();
        String passNumber = CodeGenerator.generatePassNumber();
        String verificationCode = CodeGenerator.generateVerificationCode();
        String currency = "GBP";
        String accountNo = CodeGenerator.generateAccountNumber();
        String sortCode = CodeGenerator.generateSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;

        String newPhoneNumber = "+447911894584";
        String newEmailAddress = "testnew@gmail.com";
        Account accountToSave = new Account(id, name, surname, bank, newPhoneNumber, newEmailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);

        String existingPhoneNumber = "+447587155942";
        String existingEmailAddress = "william.cuthbert@fisglobal.com";
        Account accountExisting = new Account(id, name, surname, bank, existingPhoneNumber, existingEmailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);

        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(accountExisting));
        when(accountRepository.findById(id)).thenReturn(Optional.of(accountExisting));
        when(accountRepository.save(accountToSave)).thenReturn(accountToSave);
        accountServiceImpl.updateAccount(accountToSave);
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
        verify(accountRepository).findById(id);
        verify(accountRepository).save(accountToSave);
    }
}
