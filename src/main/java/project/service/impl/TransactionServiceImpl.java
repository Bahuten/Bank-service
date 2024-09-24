package project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.enums.Payment;
import project.exception.AccountNotFoundException;
import project.exception.TransactionNotFoundException;
import project.repository.AccountRepository;
import project.repository.TransactionRepository;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.service.interfaces.LoginService;
import project.service.interfaces.TransactionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CurrencyExchangeServiceImpl currencyExchangeService;
    private final LoginService loginService;

    @Autowired
    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository,
                                  CurrencyExchangeServiceImpl currencyExchangeService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.currencyExchangeService = currencyExchangeService;
        this.loginService = new LoginServiceImpl(accountRepository);
    }

    @Override
    public Transaction getTransactionById(String id) {
        log.info("Getting transaction..." + " id=" + id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id=" + id + " could not be found"));
        log.info("Transaction retrieved" + " id=" + id);
        return transaction;
    }

    @Override
    public Transaction sendAmount(String id, String currentAccountId, String targetAccountId, double amount, String reference, Payment paymentType, Transaction transaction) {
        String initiatedDate = LocalDateTime.now().format(dateTimeFormatter);
        Account currentAccount = accountRepository.findById(currentAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with id=" + currentAccountId  + "could not be found"));
        Transaction transactionToSave = new Transaction();
        if (loginService.isLoggedOn(currentAccount.getCustomerNumber(), currentAccount.getPassNumber())) {
            if (targetAccountId == null) {
                if (paymentType.equals(Payment.WITHDRAW)) {
                    log.info("Setting up a withdraw part 1..." + " id=" + id + " current account id=" + currentAccountId + " target account id=" + targetAccountId + " amount=" + amount + " reference=" + reference);
                    if (isAmountPayable(amount, currentAccount.getBalance(), Payment.WITHDRAW)) {
                        log.info("Withdrawal amount validated");
                        updateBalance(currentAccount, null, amount, Payment.WITHDRAW);
                        String completedDate = LocalDateTime.now().format(dateTimeFormatter);
                        transactionToSave = transactionRepository.save(new Transaction(id, currentAccount.getId(), null, currentAccount.getFirstName(), currentAccount.getSurname(), amount, initiatedDate, completedDate, reference, paymentType));
                    } else {
                        log.info("Withdraw amount payable is not valid");
                        transactionToSave = null;
                    }
                } else {
                    log.info("Setting up a deposit part 1..." + " id=" + id + " current account id=" + currentAccountId + " target account id=" + targetAccountId + " amount=" + amount + " reference=" + reference);
                    if (isAmountPayable(amount, currentAccount.getBalance(), Payment.DEPOSIT)) {
                        log.info("Deposit amount validated");
                        updateBalance(currentAccount, null, amount, Payment.DEPOSIT);
                        String completedDate = LocalDateTime.now().format(dateTimeFormatter);
                        transactionToSave = transactionRepository.save(new Transaction(id, currentAccount.getId(), null, currentAccount.getFirstName(), currentAccount.getSurname(), amount, initiatedDate, completedDate, reference, paymentType));
                    } else {
                        log.info("Deposit amount payable is not valid");
                        transactionToSave = null;
                    }
                }
            } else {
                Account targetAccount = accountRepository.findById(targetAccountId)
                        .orElseThrow(() -> new AccountNotFoundException("Account with id=" + targetAccountId  + "could not be found"));
                double convertedAmount;
                if (paymentType.equals(Payment.REFUND)) {
                    log.info("Setting up a refund part 1..." + " id=" + id + " current account id=" + currentAccountId + " target account id=" + targetAccountId + " amount=" + amount + " reference=" + reference);
                    if (isAmountPayable(amount, transaction.getAmount(), Payment.REFUND)) {
                        log.info("Refund amount validated");
                        convertedAmount = currencyExchangeService.getConvertedCurrencyAmount(targetAccount.getCurrency(), currentAccount.getCurrency(), amount);
                        updateBalance(currentAccount, targetAccount, amount, Payment.REFUND);
                        String completedDate = LocalDateTime.now().format(dateTimeFormatter);
                        transactionToSave = transactionRepository.save(new Transaction(id, currentAccount.getId(), targetAccount.getId(), currentAccount.getFirstName(), currentAccount.getSurname(), convertedAmount, initiatedDate, completedDate, reference, paymentType));
                    } else {
                        log.info("Refund amount payable is not valid");
                        transactionToSave = null;
                    }
                } else {
                    log.info("Setting up a transfer part 1..." + " id=" + id + " current account id=" + currentAccountId + " target account id=" + targetAccountId + " amount=" + amount + " reference=" + reference);
                    if (isAmountPayable(amount, currentAccount.getBalance(), Payment.TRANSFER)) {
                        log.info("Transfer amount validated");
                        convertedAmount = currencyExchangeService.getConvertedCurrencyAmount(currentAccount.getCurrency(), targetAccount.getCurrency(), amount);
                        updateBalance(currentAccount, targetAccount, convertedAmount, Payment.TRANSFER);
                        String completedDate = LocalDateTime.now().format(dateTimeFormatter);
                        transactionToSave = transactionRepository.save(new Transaction(id, currentAccount.getId(), targetAccount.getId(), targetAccount.getFirstName(), targetAccount.getSurname(), convertedAmount, initiatedDate, completedDate, reference, paymentType));
                    } else {
                        log.info("Transfer amount payable is not valid");
                        transactionToSave = null;
                    }
                }
            }
        }
        return transactionToSave;
    }

    public boolean isAmountPayable(double amount, double currentAccountAmount, Payment paymentType) {
        switch (paymentType) {
            case TRANSFER:
                log.info("Validating transfer amount...");
                break;
            case REFUND:
                log.info("Validating refund amount...");
                break;
            case DEPOSIT:
                log.info("Validating deposit amount...");
                break;
            case WITHDRAW:
                log.info("Validating withdraw amount...");
                break;
        }
        return (currentAccountAmount - amount) >= 0;
    }

    public void updateBalance(Account currentAccount, Account targetAccount, double amount, Payment payment) {
        if (targetAccount == null) {
            if (payment.equals(Payment.DEPOSIT)) {
                log.info("Setting up deposit..." + " current account id=" + currentAccount.getId() + " balance=" + currentAccount.getBalance() + " amount=" + amount + " payment=" + Payment.DEPOSIT);
                currentAccount.setBalance((currentAccount.getBalance() + amount));
                log.info("deposit sent..." + " current account id=" + currentAccount.getId() + " balance=" + currentAccount.getBalance() + " amount=" + amount + " payment=" + Payment.DEPOSIT);
            } else {
                log.info("Setting up withdrawal..." + " current account id=" + currentAccount.getId() + " balance=" + currentAccount.getBalance() + " amount=" + amount + " payment=" + Payment.WITHDRAW);
                currentAccount.setBalance((currentAccount.getBalance() - amount));
                log.info("Withdrawal made..." + " current account id=" + currentAccount.getId() + " balance=" + currentAccount.getBalance() + " amount=" + amount + " payment=" + Payment.WITHDRAW);
            }
        } else {
            if (payment.equals(Payment.TRANSFER)) {
                log.info("Setting up a transfer part 2..." + " current account id=" + currentAccount.getId() + " balance=" + currentAccount.getBalance() + " amount=" + amount + " payment=" + Payment.TRANSFER);
                log.info("Setting up a transfer part 3..." + " target account id=" + targetAccount.getId() + " balance=" + targetAccount.getBalance() + " amount=" + amount + " payment=" + Payment.TRANSFER);
                currentAccount.setBalance((currentAccount.getBalance() - amount));
                targetAccount.setBalance((targetAccount.getBalance() + amount));
                log.info("transfer sent part 1..." + " current account id=" + currentAccount.getId() + " balance=" + currentAccount.getBalance() + " amount=" + amount + " payment=" + Payment.TRANSFER);
                log.info("transfer sent part 2..." + " current account id=" + currentAccount.getId() + " balance=" + currentAccount.getBalance() + " amount=" + amount + " payment=" + Payment.TRANSFER);
            } else {
                log.info("Setting up a refund part 2..." + " current account id=" + currentAccount.getId() + " balance=" + currentAccount.getBalance() + " amount=" + amount + " payment=" + Payment.REFUND);
                log.info("Setting up a refund part 3..." + " current account id=" + targetAccount.getId() + " balance=" + targetAccount.getBalance() + " amount=" + amount + " payment=" + Payment.REFUND);
                currentAccount.setBalance((currentAccount.getBalance() + amount));
                targetAccount.setBalance((targetAccount.getBalance() - amount));
                log.info("refund sent part 1..." + " current account id=" + currentAccount.getId() + " balance=" + currentAccount.getBalance() + " amount=" + amount + " payment=" + Payment.REFUND);
                log.info("refund sent part 2..." + " target account id=" + targetAccount.getId() + " balance=" + targetAccount.getBalance() + " amount=" + amount + " payment=" + Payment.REFUND);
            }
        }
        accountRepository.save(currentAccount);
    }
}
