package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.converter.TransactionConverter;
import project.enums.Payment;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.model.requests.transaction.DepositRequest;
import project.model.requests.transaction.RefundRequest;
import project.model.requests.transaction.TransferRequest;
import project.model.requests.transaction.WithdrawRequest;
import project.model.responses.transaction.TransactionResponse;
import project.service.impl.AccountServiceImpl;
import project.service.impl.TransactionServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

import static project.utility.CommonUtils.*;

@RestController
public class TransactionRestController {
    private final AccountServiceImpl accountService;
    private final TransactionServiceImpl transactionService;

    @Autowired
    public TransactionRestController(AccountServiceImpl accountService, TransactionServiceImpl transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping(value = TRANSFER_TARGET_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> postTransfer(@PathVariable @NotBlank String currentAccountId, @PathVariable @NotBlank String targetAccountId, @Valid @RequestBody TransferRequest transferRequest) {
        Account currentAccount = accountService.getAccountById(currentAccountId);
        Account targetAccount = accountService.getAccountById(targetAccountId);
        Transaction transaction = transactionService.sendAmount(UUID.randomUUID().toString(), currentAccount.getId(), targetAccount.getId(), transferRequest.getAmount(), transferRequest.getReference(), Payment.TRANSFER, null);
        return new ResponseEntity<>(TransactionConverter.convertToTransactionResponse(transaction), HttpStatus.OK);
    }

    @PostMapping(value = DEPOSIT_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> postDeposit(@Valid @RequestBody DepositRequest depositRequest) {
        Account account = accountService.getAccountByNumber(depositRequest.getAccountNumber());
        Transaction transaction = transactionService.sendAmount(UUID.randomUUID().toString(), account.getId(), null, depositRequest.getAmount(), DEPOSIT_DEFAULT_REF, Payment.DEPOSIT, null);
        return new ResponseEntity<>(TransactionConverter.convertToTransactionResponse(transaction), HttpStatus.OK);
    }

    @PostMapping(value = WITHDRAWAL_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> postWithdraw(@Valid @RequestBody WithdrawRequest withdrawRequest) {
        Account account = accountService.getAccountBySortCodeAndAccountNumber(withdrawRequest.getSortCode(), withdrawRequest.getAccountNumber());
        Transaction transaction = transactionService.sendAmount(UUID.randomUUID().toString(), account.getId(), null, withdrawRequest.getAmount(), WITHDRAW_DEFAULT_REF, Payment.WITHDRAW, null);
        return new ResponseEntity<>(TransactionConverter.convertToTransactionResponse(transaction), HttpStatus.OK);
    }

    @PostMapping(value = REFUND_TARGET_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> postRefund(@PathVariable @NotBlank String currentAccountId, @PathVariable @NotBlank String targetAccountId, @Valid @RequestBody RefundRequest refundRequest) {
        Account targetAccount = accountService.getAccountById(targetAccountId);
        Account currentAccount = accountService.getAccountById(currentAccountId);
        Transaction transaction = transactionService.getTransactionById(refundRequest.getTransactionId());
        transactionService.sendAmount(UUID.randomUUID().toString(), currentAccount.getId(), targetAccount.getId(), refundRequest.getAmount(), transaction.getReference(), Payment.REFUND, transaction);
        return new ResponseEntity<>(TransactionConverter.convertToTransactionResponse(transaction), HttpStatus.OK);
    }
}
