package project.model.responses.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.repository.entity.Account;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountsResponse {
    private List<Account> Accounts;
}
