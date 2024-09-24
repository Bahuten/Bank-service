package project.repository;

import org.springframework.data.repository.CrudRepository;
import project.repository.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, String> {
    Optional<Account> findBySortCodeAndAccountNumber(String sort, String accountNum);
    Optional<Account> findById(String id);
    Optional<Account> findAccountByAccountNumber(String accountNumber);
    void deleteById(String id);
    List<Account> findAll();
    Optional<Account> findAccountByCustomerNumberAndPassNumber(String customerNumber, String passNumber);
}