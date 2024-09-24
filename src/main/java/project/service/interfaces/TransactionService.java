package project.service.interfaces;

import project.enums.Payment;
import project.repository.entity.Account;
import project.repository.entity.Transaction;

public interface TransactionService {
    Transaction getTransactionById(String id);

    boolean isAmountPayable(double amount, double currentAmount, Payment paymentType);

    Transaction sendAmount(String id, String currentAccountId, String targetAccountId, double amount, String reference, Payment paymentType, Transaction transactionId);

    void updateBalance(Account currentAccount, Account targetAccount, double amount, Payment paymentType);
}
