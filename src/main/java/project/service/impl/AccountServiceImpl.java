package project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.enums.LoginStatus;
import project.exception.AccountExistsException;
import project.exception.AccountNotFoundException;
import project.repository.entity.Account;
import project.repository.AccountRepository;
import project.repository.TransactionRepository;
import project.service.interfaces.AccountService;
import project.service.interfaces.LoginService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LoginService loginService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.loginService = new LoginServiceImpl(this.accountRepository);
    }

    @Override
    public Account createAccount(String id, String bankName, String firstName, String surname, String phoneNumber, String emailAddress, String customerNumber, String passNumber, String verificationCode, String currency, String sortCode, String accountNumber) {
        log.info("Creating account..." + " id=" + id + " bankName=" + bankName + " firstName=" + firstName + " surname=" + surname + " sortCode=" + sortCode + " accountNumber=" + accountNumber);
        doesAccountExists(sortCode, accountNumber);
        Account account = accountRepository.save(new Account(id, firstName, surname, bankName, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, 0.00, currency, new ArrayList<>(), LoginStatus.LOGIN_ACCESS_GRANTED));
        log.info("Account created..." + " id=" + id + " bankName=" + bankName + " firstName=" + firstName + " surname=" + surname + " sortCode=" + sortCode + " accountNumber=" + accountNumber);
        return account;
    }

    @Override
    public Account getAccountBySortCodeAndAccountNumber(String sortCode, String accountNumber) {
        log.info("Getting account..." + " sortCode=" + sortCode + " accountNumber=" + accountNumber);
        Account account = accountRepository.findBySortCodeAndAccountNumber(sortCode, accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account with sort code=" + sortCode + " and account number=" + accountNumber + " could not be found"));
        account.setTransactions(transactionRepository.findBySourceAccountIdOrderByInitiationDate(account.getId()));
        log.info("Account retrieved..." + " sortCode=" + sortCode + " accountNumber=" + accountNumber);
        return account;
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        log.info("Getting account..." + " accountNumber=" + accountNumber);
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account with account number=" + accountNumber + " could not be found"));
        account.setTransactions(transactionRepository.findBySourceAccountIdOrderByInitiationDate(account.getId()));
        log.info("Account retrieved..." + " accountNumber=" + accountNumber);
        return account;
    }

    @Override
    public Account getAccountById(String id) {
        log.info("Getting account..." + " id=" + id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id=" + id  + "could not be found"));
        account.setTransactions(transactionRepository.findBySourceAccountIdOrderByInitiationDate(account.getId()));
        log.info("Account retrieved..." + " id=" + id);
        return account;
    }

    @Override
    public List<Account> getAccounts() {
        log.info("Getting all accounts...");
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            account.setTransactions(transactionRepository.findBySourceAccountIdOrderByInitiationDate(account.getId()));
        }
        log.info("All Accounts retrieved...");
        return accounts;
    }

    @Override
    public Account updateAccount(Account account) {
        log.info("checking the login status for the account user before updating...");
        if (loginService.isLoggedOn(account.getCustomerNumber(), account.getPassNumber())) {
            log.info("Updating account..." + " id=" + account.getId());
            Account searchAccount = getAccountById(account.getId());
            if (!searchAccount.getSortCode().equals(account.getSortCode()) &&
                    !searchAccount.getAccountNumber().equals(account.getAccountNumber())) {
                doesAccountExists(account.getSortCode(), account.getAccountNumber());
            }
            Account updatedAccount = accountRepository.save(account);
            log.info("Account updated..." + " id=" + account.getId());
            return updatedAccount;
        } else {
            log.error("account user is not logged on to update the account associating with: " + account.getId());
            return null;
        }
    }

    @Override
    public void deleteAccount(String id) {
        Account account = getAccountById(id);
        if (loginService.isLoggedOn(account.getCustomerNumber(), account.getPassNumber())) {
            log.info("Deleting account..." + " id=" + id);
            accountRepository.deleteById(account.getId());
            log.info("Account deleted..." + " id=" + id);
        } else {
            log.error("account user is not logged on to delete the account associating with: " + account.getId());
        }
    }

    private void doesAccountExists(String sortCode, String accountNumber) {
        log.info("Checking for existing account..." + " " + "sort code=" + sortCode + " " + "account number=" + accountNumber);
        Optional<Account> existingAccount = accountRepository.findBySortCodeAndAccountNumber(sortCode, accountNumber);
        if (existingAccount.isPresent()) {
            throw new AccountExistsException("Account exists with sort code=" + sortCode + " " + "account number=" + accountNumber);
        }
        log.info("Checking for existing account..." + " " + "sort code=" + sortCode + " " + "account number=" + accountNumber);
    }
}
