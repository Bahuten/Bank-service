package project.service.impl;

import org.springframework.stereotype.Service;
import project.enums.LoginStatus;
import project.exception.AccountNotFoundException;
import project.exception.AccountVerificationFailureException;
import project.repository.AccountRepository;
import project.repository.entity.Account;
import project.service.interfaces.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    private final AccountRepository accountRepository;

    public LoginServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean isLoggedOn(String customerNumber, String passNumber) {
        return accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account with customer number=" + customerNumber + " and pass number=" + passNumber + " could not be found"))
                .getLoginStatus()
                .getStatus();
    }

    @Override
    public Account openSession(String customerNumber, String passNumber) {
        return accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account with customer number=" + customerNumber + " and pass number=" + passNumber + " could not be found"));
    }

    @Override
    public Account closeSession(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id=" + id + " could not be found"));
        if (account != null) {
            account.setLoginStatus(LoginStatus.LOGIN_ACCESS_FAILED);
        }
        return account;
    }

    @Override
    public boolean verifyLogin(String id, String verificationCode) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountVerificationFailureException("Account with id=" + id + " could not be verified, Please try to re-login again."))
                .getVerificationCode()
                .equals(verificationCode);
    }
}
