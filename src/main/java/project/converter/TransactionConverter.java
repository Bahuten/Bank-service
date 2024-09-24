package project.converter;

import project.repository.entity.Transaction;
import project.model.responses.transaction.TransactionResponse;

public class TransactionConverter {

    public static TransactionResponse convertToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getSourceAccountId(),
                transaction.getTargetAccountId(),
                transaction.getFirstName(),
                transaction.getSurname(),
                transaction.getAmount(),
                transaction.getInitiationDate(),
                transaction.getCompletionDate(),
                transaction.getReference(),
                transaction.getPaymentType()
        );
    }

    public static Transaction convertTransactionResponseToTransaction(TransactionResponse transactionResponse) {
        return new Transaction(
                transactionResponse.getId(),
                transactionResponse.getSourceAccountId(),
                transactionResponse.getTargetAccountId(),
                transactionResponse.getFirstName(),
                transactionResponse.getSurname(),
                transactionResponse.getAmount(),
                transactionResponse.getInitiationDate(),
                transactionResponse.getCompletionDate(),
                transactionResponse.getReference(),
                transactionResponse.getPaymentType()
        );
    }
}
