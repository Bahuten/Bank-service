package project.repository;

import org.springframework.data.repository.CrudRepository;
import project.repository.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, String> {
    List<Transaction> findBySourceAccountIdOrderByInitiationDate(String id);
    Optional<Transaction> findById(String id);
}
