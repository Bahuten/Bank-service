package project.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import project.enums.LoginStatus;
import project.enums.Payment;
import project.exception.AccountNotFoundException;
import project.exception.TransactionNotFoundException;
import project.repository.AccountRepository;
import project.repository.TransactionRepository;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.service.impl.CurrencyExchangeServiceImpl;
import project.service.impl.TransactionServiceImpl;
import project.utility.CodeGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    private static DateTimeFormatter dateTimeFormatter;
    @Mock
    Account currentAccount;
    @Mock
    Account targetAccount;
    String id;
    String sourceAccountId;
    String targetAccountId;
    String name;
    String targetAccountFirstName;
    String surname;
    String targetAccountSurname;
    String bankName;
    String phoneNumber;
    String targetAccountPhoneNumber;
    String emailAddress;
    String targetAccountEmailAddress;
    String customerNumber;
    String targetAccountCustomerNumber;
    String passNumber;
    String targetAccountPassNumber;
    String verificationCode;
    String targetAccountVerificationCode;
    String currency;
    String targetAccountCurrency;
    double balance;
    double amount;
    String reference;
    String accountNumber;
    String targetAccountAccountNumber;
    String sortCode;
    String targetAccountSortCode;
    String startDate;
    String endDate;
    Payment Transfer;
    Payment Refund;
    Payment Deposit;
    Payment Withdraw;
    LoginStatus login;
    LoginStatus loggedOff;
    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CurrencyExchangeServiceImpl currencyExchangeService;

    @BeforeAll
    static void setUpDateTimeFormatter() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    }

    private static Stream<Arguments> amountBalance() {
        return Stream.of(arguments(200, 200, Payment.TRANSFER, true),
                arguments(200, 250, Payment.TRANSFER, true),
                arguments(150, 100, Payment.TRANSFER, false),
                arguments(200, 200, Payment.REFUND, true),
                arguments(200, 250, Payment.REFUND, true),
                arguments(150, 100, Payment.REFUND, false),
                arguments(200, 200, Payment.WITHDRAW, true),
                arguments(200, 250, Payment.WITHDRAW, true),
                arguments(150, 100, Payment.WITHDRAW, false),
                arguments(200, 200, Payment.DEPOSIT, true),
                arguments(200, 250, Payment.DEPOSIT, true),
                arguments(150, 100, Payment.DEPOSIT, false));
    }

    private static Stream<Arguments> updateBalance() {
        return Stream.of(arguments(30, Payment.DEPOSIT),
                arguments(30, Payment.WITHDRAW),
                arguments(30, Payment.TRANSFER),
                arguments(30, Payment.REFUND));
    }

    @BeforeEach
    void setUpData() {
        id = UUID.randomUUID().toString();
        sourceAccountId = UUID.randomUUID().toString();
        targetAccountId = UUID.randomUUID().toString();
        name = "Martin";
        targetAccountFirstName = "James";
        surname = "King";
        targetAccountSurname = "Anderson";
        bankName = "NatWest";
        phoneNumber = "+4473458999125";
        targetAccountPhoneNumber = "+4479634177928";
        emailAddress = "current@fisglobal.com";
        targetAccountEmailAddress = "target@fisglobal.com";
        customerNumber = CodeGenerator.generateCustomerNumber();
        targetAccountCustomerNumber = CodeGenerator.generateCustomerNumber();
        passNumber = CodeGenerator.generatePassNumber();
        targetAccountPassNumber = CodeGenerator.generatePassNumber();
        verificationCode = CodeGenerator.generateVerificationCode();
        targetAccountVerificationCode = CodeGenerator.generateVerificationCode();
        currency = "GBP";
        targetAccountCurrency = "GBP";
        balance = 500.00;
        amount = 20.00;
        reference = "unit-test";
        accountNumber = CodeGenerator.generateAccountNumber();
        targetAccountAccountNumber = CodeGenerator.generateAccountNumber();
        sortCode = CodeGenerator.generateSortCode();
        targetAccountSortCode = CodeGenerator.generateSortCode();
        startDate = LocalDateTime.now().format(dateTimeFormatter);
        endDate = LocalDateTime.now().format(dateTimeFormatter);
        Transfer = Payment.TRANSFER;
        Refund = Payment.REFUND;
        Deposit = Payment.DEPOSIT;
        Withdraw = Payment.WITHDRAW;
        login = LoginStatus.LOGIN_ACCESS_GRANTED;
        loggedOff = LoginStatus.LOGIN_ACCESS_FAILED;
        currentAccount = new Account(sourceAccountId, name, surname, bankName, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), login);
        targetAccount = new Account(targetAccountId, targetAccountFirstName, targetAccountSurname, bankName, targetAccountPhoneNumber, targetAccountEmailAddress, targetAccountCustomerNumber, targetAccountPassNumber,
                targetAccountVerificationCode, targetAccountAccountNumber, targetAccountSortCode, balance, targetAccountCurrency, new ArrayList<>(), loggedOff);
    }

    @Test
    public void postTransfer() {
        Transaction transactionExpected = new Transaction(id, sourceAccountId, targetAccountId, targetAccountFirstName, targetAccountSurname, amount, startDate, endDate, reference, Transfer);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.of(targetAccount));
        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(currentAccount));
        when(transactionRepository.save(transactionExpected)).thenReturn(transactionExpected);
        when(currencyExchangeService.getConvertedCurrencyAmount(currency, targetAccountCurrency, amount)).thenReturn(amount);
        Transaction transactionActual = transactionService.sendAmount(id, sourceAccountId, targetAccountId, amount, reference, Transfer, null);
        verify(transactionRepository).save(transactionExpected);
        verify(transactionRepository).save(transactionActual);
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findById(targetAccountId);
        verify(currencyExchangeService).getConvertedCurrencyAmount(currency, targetAccountCurrency, amount);
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
        assertEquals(transactionExpected, transactionActual);
    }

    @Test
    public void postTransferWhenTargetAccountMissing() {
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.empty());
        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(currentAccount));
        assertThrows(AccountNotFoundException.class, () -> transactionService.sendAmount(id, sourceAccountId, targetAccountId, amount, reference, Transfer, null));
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findById(targetAccountId);
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
    }

    @Test
    public void postTransferWhenCurrentAccountMissing() {
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.sendAmount(id, sourceAccountId, targetAccountId, amount, reference, Transfer, null));
        verify(accountRepository).findById(sourceAccountId);
    }

    @Test
    public void getTransactionId() {
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, name, surname, amount, startDate, endDate, reference, Transfer);
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
        Transaction transactionActual = transactionService.getTransactionById(id);
        verify(transactionRepository).findById(id);
        assertEquals(transaction, transactionActual);
    }

    @Test
    public void getTransactionIdMissing() {
        when(transactionRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(id));
        verify(transactionRepository).findById(id);
    }

    @Test
    public void postDeposit() {
        Transaction transaction = new Transaction(id, sourceAccountId, null, name, surname, amount, startDate, endDate, reference, Deposit);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(currentAccount));
        Transaction transactionActual = transactionService.sendAmount(id, sourceAccountId, null, amount, reference, Deposit, null);
        verify(transactionRepository).save(transaction);
        verify(transactionRepository).save(transactionActual);
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
        assertEquals(transaction, transactionActual);
    }

    @Test
    public void postDepositWhenCurrentAccountMissing() {
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.sendAmount(id, sourceAccountId, null, amount, reference, Deposit, null));
        verify(accountRepository).findById(sourceAccountId);
    }

    @Test
    public void postWithdrawal() {
        Transaction transactionExpected = new Transaction(id, sourceAccountId, null, name, surname, amount, startDate, endDate, reference, Withdraw);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(transactionRepository.save(transactionExpected)).thenReturn(transactionExpected);
        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(currentAccount));
        Transaction transactionActual = transactionService.sendAmount(id, sourceAccountId, null, amount, reference, Withdraw, null);
        verify(transactionRepository).save(transactionExpected);
        verify(transactionRepository).save(transactionActual);
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
        assertEquals(transactionExpected, transactionActual);
    }

    @Test
    public void postWithdrawWhenCurrentAccountMissing() {
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.sendAmount(id, sourceAccountId, null, amount, reference, Withdraw, null));
        verify(accountRepository).findById(sourceAccountId);
    }

    @Test
    public void postRefund() {
        Transaction transactionExpected = new Transaction(id, sourceAccountId, targetAccountId, name, surname, amount, startDate, endDate, reference, Refund);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.of(targetAccount));
        when(transactionRepository.save(transactionExpected)).thenReturn(transactionExpected);
        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(currentAccount));
        when(currencyExchangeService.getConvertedCurrencyAmount(targetAccountCurrency, currency, amount)).thenReturn(amount);
        Transaction transactionActual = transactionService.sendAmount(id, sourceAccountId, targetAccountId, amount, reference, Refund, transactionExpected);
        verify(transactionRepository).save(transactionExpected);
        verify(transactionRepository).save(transactionActual);
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findById(targetAccountId);
        verify(currencyExchangeService).getConvertedCurrencyAmount(targetAccountCurrency, currency, amount);
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
        assertEquals(transactionExpected, transactionActual);
    }

    @Test
    public void postRefundWhenTargetAccountMissing() {
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, name, surname, amount, startDate, endDate, reference, Refund);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(currentAccount));
        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.empty());
        when(accountRepository.findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber)).thenReturn(Optional.of(currentAccount));
        assertThrows(AccountNotFoundException.class, () -> transactionService.sendAmount(id, sourceAccountId, targetAccountId, amount, reference, Refund, transaction));
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findById(targetAccountId);
        verify(accountRepository).findAccountByCustomerNumberAndPassNumber(customerNumber, passNumber);
    }

    @Test
    public void postRefundWhenCurrentAccountMissing() {
        Transaction transaction = new Transaction(id, sourceAccountId, targetAccountId, name, surname, amount, startDate, endDate, reference, Refund);
        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.sendAmount(id, sourceAccountId, targetAccountId, amount, reference, Refund, transaction));
        verify(accountRepository).findById(sourceAccountId);
    }

    @ParameterizedTest
    @MethodSource("amountBalance")
    public void verifyIsAmountPayable(double amount, double balance, Payment paymentType, boolean expected) {
        assertEquals(transactionService.isAmountPayable(amount, balance, paymentType), expected);
    }

    @ParameterizedTest
    @MethodSource("updateBalance")
    public void verifyBalanceUpdate(int amount, Payment payment) {
        Double beforeAccountOneUpdateBalance = currentAccount.getBalance();
        Double beforeAccountTwoUpdateBalance = targetAccount.getBalance();

        if (payment.equals(Payment.TRANSFER) || payment.equals(Payment.REFUND)) {
            transactionService.updateBalance(currentAccount, targetAccount, amount, payment);
            assertNotEquals(beforeAccountOneUpdateBalance, currentAccount.getBalance());
            assertNotEquals(beforeAccountTwoUpdateBalance, targetAccount.getBalance());
        } else {
            transactionService.updateBalance(currentAccount, null, amount, payment);
            assertNotEquals(beforeAccountOneUpdateBalance, currentAccount.getBalance());
        }
    }
}
