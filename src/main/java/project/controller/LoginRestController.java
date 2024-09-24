package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.converter.AccountConverter;
import project.enums.LoginStatus;
import project.model.requests.LoginRequest;
import project.repository.entity.Account;

import project.service.interfaces.AccountService;
import project.service.interfaces.LoginService;
import project.utility.CodeGenerator;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static project.utility.CommonUtils.*;

@RestController
public class LoginRestController {

    private final LoginService loginService;
    private final AccountService accountService;

    @Autowired
    public LoginRestController(LoginService loginService, AccountService accountService) {
        this.loginService = loginService;
        this.accountService = accountService;
    }

    @PostMapping(value = LOGIN_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginInToAccount(@Valid @RequestBody LoginRequest loginRequest) {
        loginService.openSession(loginRequest.getCustomerNumber(), loginRequest.getPassNumber());
        return new ResponseEntity<>("Sent a text to your phone number to verify login", HttpStatus.OK);
    }

    @PostMapping(value = LOGOUT_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logoutOfAccount(@PathVariable @NotBlank String currentAccountId) {
        loginService.closeSession(currentAccountId);
        return new ResponseEntity<>("You have logged out of your account", HttpStatus.OK);
    }

    @PostMapping(value = VERIFY_LOGIN_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyLogin(@PathVariable @NotBlank String currentAccountId, @PathVariable @NotBlank String verificationCode) {
        boolean verified = loginService.verifyLogin(currentAccountId, verificationCode);
        if (verified) {
            Account updateAccount = accountService.getAccountById(currentAccountId);
            updateAccount.setVerificationCode(CodeGenerator.generateVerificationCode());
            updateAccount.setLoginStatus(LoginStatus.LOGIN_ACCESS_GRANTED);
            accountService.updateAccount(updateAccount);
            return new ResponseEntity<>(AccountConverter.convertToAccountResponse(updateAccount), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account verification failed", HttpStatus.FORBIDDEN);
        }
    }
}
