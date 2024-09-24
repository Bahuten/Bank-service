package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.converter.AccountConverter;
import project.model.requests.account.UpdateAccountRequest;
import project.model.responses.account.AccountResponse;
import project.model.responses.account.AccountsResponse;
import project.model.requests.account.CreateAccountRequest;
import project.repository.entity.Account;
import project.service.interfaces.AccountService;
import project.service.impl.AccountServiceImpl;
import project.utility.CodeGenerator;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

import static project.utility.CommonUtils.*;

@RestController
public class AccountRestController {
    private final AccountService accountService;

    @Autowired
    public AccountRestController(AccountServiceImpl accountServiceImpl) {
        this.accountService = accountServiceImpl;
    }

    @PostMapping(value = ACCOUNT_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        Account account = accountService.createAccount(
                UUID.randomUUID().toString(),
                createAccountRequest.getBankName(),
                createAccountRequest.getFirstName(),
                createAccountRequest.getSurname(),
                createAccountRequest.getPhoneNumber(),
                createAccountRequest.getEmailAddress(),
                CodeGenerator.generateCustomerNumber(),
                CodeGenerator.generatePassNumber(),
                CodeGenerator.generateVerificationCode(),
                createAccountRequest.getCurrency(),
                CodeGenerator.generateSortCode(),
                CodeGenerator.generateAccountNumber()
        );
        return new ResponseEntity<>(AccountConverter.convertToAccountResponse(account), HttpStatus.CREATED);
    }

    @GetMapping(value = ACCOUNT_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable @NotBlank String currentAccountId) {
        Account account = accountService.getAccountById(currentAccountId);
        return new ResponseEntity<>(AccountConverter.convertToAccountResponse(account), HttpStatus.OK);
    }

    @GetMapping(value = ACCOUNT_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> getAccountBySortCodeAndAccountNumber(@RequestParam @Valid String sortCode, @RequestParam @Valid String accountNumber) {
        Account account = accountService.getAccountBySortCodeAndAccountNumber(sortCode, accountNumber);
        return new ResponseEntity<>(AccountConverter.convertToAccountResponse(account), HttpStatus.OK);
    }

    @GetMapping(value = ACCOUNTS_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountsResponse> getAccounts() {
        List<Account> accounts = accountService.getAccounts();
        return new ResponseEntity<>(AccountConverter.convertAccountsToAccountsResponse(accounts), HttpStatus.OK);
    }

    @PutMapping(value = ACCOUNT_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> putAccount(@PathVariable @NotBlank String currentAccountId, @RequestBody @Valid UpdateAccountRequest updateAccountRequest) {
        Account updateAccount = accountService.updateAccount(AccountConverter.convertAccountRequestToAccount(currentAccountId, updateAccountRequest, accountService.getAccountById(currentAccountId)));
        return new ResponseEntity<>(AccountConverter.convertToAccountResponse(updateAccount), HttpStatus.CREATED);
    }

    @DeleteMapping(value = ACCOUNT_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> deleteAccount(@PathVariable @NotBlank String currentAccountId) {
        accountService.deleteAccount(currentAccountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
