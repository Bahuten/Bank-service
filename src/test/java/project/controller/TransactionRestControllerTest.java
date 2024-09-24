package project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.enums.LoginStatus;
import project.enums.Payment;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.model.requests.transaction.DepositRequest;
import project.model.requests.transaction.RefundRequest;
import project.model.requests.transaction.TransferRequest;
import project.model.requests.transaction.WithdrawRequest;
import project.service.impl.AccountServiceImpl;
import project.service.impl.TransactionServiceImpl;
import project.utility.CodeGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static project.utility.CommonUtils.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(TransactionRestController.class)
public class TransactionRestControllerTest {

    private static DateTimeFormatter dateTimeFormatter;
    @MockBean
    private TransactionServiceImpl transactionService;
    @MockBean
    private AccountServiceImpl accountService;
    @Autowired
    private MockMvc mockMvc;
    private String id;
    private String sourceAccountId;
    private String targetAccountId;
    private String name;
    private String surname;
    private String bank;
    private String phoneNumber;
    private String emailAddress;
    private String customerNumber;
    private String passNumber;
    private String verificationCode;
    private String accountNumber;
    private String sortCode;
    private String reference;
    private double amount;
    private double balance;
    private String currency;
    private String startTransaction;
    private String endTransaction;
    private LoginStatus loggedOff;
    private LoginStatus login;

    @BeforeAll
    static void setUpDateTimeFormatter() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    }

    @BeforeEach
    void setUpAccountDetails() {
        id = UUID.randomUUID().toString();
        sourceAccountId = UUID.randomUUID().toString();
        targetAccountId = UUID.randomUUID().toString();
        name = "Martin";
        surname = "King";
        bank = "NatWest";
        phoneNumber = "+447587155942";
        emailAddress = "william.cuthbert@fisglobal.com";
        customerNumber = CodeGenerator.generateCustomerNumber();
        passNumber = CodeGenerator.generatePassNumber();
        verificationCode = CodeGenerator.generateVerificationCode();
        currency = "GBP";
        balance = 500.00;
        amount = 20.00;
        reference = "unit test";
        accountNumber = CodeGenerator.generateAccountNumber();
        sortCode = CodeGenerator.generateSortCode();
        startTransaction = LocalDateTime.now().format(dateTimeFormatter);
        endTransaction = LocalDateTime.now().format(dateTimeFormatter);
        login = LoginStatus.LOGIN_ACCESS_GRANTED;
        loggedOff = LoginStatus.LOGIN_ACCESS_FAILED;
    }

    @Test
    void postTransfer() throws Exception {
        Account sourceAccount = new Account(sourceAccountId, bank, name, surname, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), login);
        Account targetAccount = new Account(targetAccountId, bank, name, surname, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), loggedOff);
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, name, surname, amount, startTransaction, endTransaction, reference, Payment.TRANSFER);
        TransferRequest transferRequest = new TransferRequest(amount, reference);
        when(accountService.getAccountById(sourceAccount.getId())).thenReturn(sourceAccount);
        when(accountService.getAccountById(targetAccount.getId())).thenReturn(targetAccount);
        when(transactionService.sendAmount(anyString(), eq(sourceAccountId), eq(targetAccountId), eq(transferRequest.getAmount()), eq(transferRequest.getReference()), eq(Payment.TRANSFER), eq(null))).thenReturn(transaction);
        mockMvc.perform(MockMvcRequestBuilders.post(TRANSFER_TARGET_ENDPOINT, sourceAccountId, targetAccountId)
                        .content(new ObjectMapper().writeValueAsString(transferRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.sourceAccountId", is(sourceAccountId)))
                .andExpect(jsonPath("$.targetAccountId", is(targetAccountId)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.surname", is(surname)))
                .andExpect(jsonPath("$.amount", is(amount)))
                .andExpect(jsonPath("$.initiationDate", is(startTransaction)))
                .andExpect(jsonPath("$.completionDate", is(endTransaction)))
                .andExpect(jsonPath("$.reference", is(reference)))
                .andExpect(jsonPath("$.paymentType", is(Payment.TRANSFER.getValue())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        verify(transactionService).sendAmount(anyString(), eq(sourceAccountId), eq(targetAccountId), eq(transferRequest.getAmount()), eq(transferRequest.getReference()), eq(Payment.TRANSFER), eq(null));
    }

    @Test
    void postRefund() throws Exception {
        Account sourceAccount = new Account(sourceAccountId, bank, name, surname, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), login);
        Account targetAccount = new Account(targetAccountId, bank, name, surname, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), login);
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, name, surname, amount, startTransaction, endTransaction, reference, Payment.REFUND);
        RefundRequest refundRequest = new RefundRequest(id, amount);
        when(accountService.getAccountById(sourceAccountId)).thenReturn(sourceAccount);
        when(accountService.getAccountById(targetAccountId)).thenReturn(targetAccount);
        when(transactionService.getTransactionById(id)).thenReturn(transaction);
        mockMvc.perform(MockMvcRequestBuilders.post(REFUND_TARGET_ENDPOINT, sourceAccountId, targetAccountId)
                        .content(new ObjectMapper().writeValueAsString(refundRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.sourceAccountId", is(sourceAccountId)))
                .andExpect(jsonPath("$.targetAccountId", is(targetAccountId)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.surname", is(surname)))
                .andExpect(jsonPath("$.amount", is(amount)))
                .andExpect(jsonPath("$.initiationDate", is(startTransaction)))
                .andExpect(jsonPath("$.completionDate", is(endTransaction)))
                .andExpect(jsonPath("$.reference", is(reference)))
                .andExpect(jsonPath("$.paymentType", is(Payment.REFUND.getValue())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        verify(transactionService).sendAmount(anyString(), eq(sourceAccountId), eq(targetAccountId), eq(refundRequest.getAmount()), eq(reference), eq(Payment.REFUND), eq(transaction));
    }

    @Test
    void postDeposit() throws Exception {
        targetAccountId = null;
        Account targetAccount = new Account(sourceAccountId, bank, name, surname, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), login);
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, name, surname, amount, startTransaction, endTransaction, DEPOSIT_DEFAULT_REF, Payment.DEPOSIT);
        DepositRequest depositRequest = new DepositRequest(accountNumber, amount);
        when(accountService.getAccountByNumber(accountNumber)).thenReturn(targetAccount);
        when(transactionService.sendAmount(anyString(), eq(sourceAccountId), eq(targetAccountId), eq(amount), eq(DEPOSIT_DEFAULT_REF), eq(Payment.DEPOSIT), eq(null))).thenReturn(transaction);
        mockMvc.perform(MockMvcRequestBuilders.post(DEPOSIT_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(depositRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.sourceAccountId", is(sourceAccountId)))
                .andExpect(jsonPath("$.targetAccountId", is(targetAccountId)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.surname", is(surname)))
                .andExpect(jsonPath("$.amount", is(amount)))
                .andExpect(jsonPath("$.initiationDate", is(startTransaction)))
                .andExpect(jsonPath("$.completionDate", is(endTransaction)))
                .andExpect(jsonPath("$.reference", is(DEPOSIT_DEFAULT_REF)))
                .andExpect(jsonPath("$.paymentType", is(Payment.DEPOSIT.getValue())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        verify(transactionService).sendAmount(anyString(), eq(sourceAccountId), eq(null), eq(amount), eq(DEPOSIT_DEFAULT_REF), eq(Payment.DEPOSIT), eq(null));
    }

    @Test
    void postWithdraw() throws Exception {
        targetAccountId = null;
        Account sourceAccount = new Account(sourceAccountId, bank, name, surname, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), login);
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, name, surname, amount, startTransaction, endTransaction, WITHDRAW_DEFAULT_REF, Payment.WITHDRAW);
        WithdrawRequest withdrawRequest = new WithdrawRequest(sortCode, accountNumber, amount);
        when(accountService.getAccountBySortCodeAndAccountNumber(sortCode, accountNumber)).thenReturn(sourceAccount);
        when(transactionService.sendAmount(anyString(), eq(sourceAccountId), eq(targetAccountId), eq(amount), eq(WITHDRAW_DEFAULT_REF), eq(Payment.WITHDRAW), eq(null))).thenReturn(transaction);
        mockMvc.perform(MockMvcRequestBuilders.post(WITHDRAWAL_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(withdrawRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.sourceAccountId", is(sourceAccountId)))
                .andExpect(jsonPath("$.targetAccountId", is(targetAccountId)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.surname", is(surname)))
                .andExpect(jsonPath("$.amount", is(amount)))
                .andExpect(jsonPath("$.initiationDate", is(startTransaction)))
                .andExpect(jsonPath("$.completionDate", is(endTransaction)))
                .andExpect(jsonPath("$.reference", is(WITHDRAW_DEFAULT_REF)))
                .andExpect(jsonPath("$.paymentType", is(Payment.WITHDRAW.getValue())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        verify(transactionService).sendAmount(anyString(), eq(sourceAccountId), eq(null), eq(amount), eq(WITHDRAW_DEFAULT_REF), eq(Payment.WITHDRAW), eq(null));
    }
}