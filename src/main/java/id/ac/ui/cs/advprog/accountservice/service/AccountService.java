package id.ac.ui.cs.advprog.accountservice.service;

import id.ac.ui.cs.advprog.accountservice.model.Account;

import java.util.Optional;

public interface AccountService {
    Account createAccount(Account account);
    Optional<Account> getAccountByEmail(String email);
    Optional<Account> getAccountByUsername(String username);
    Optional<String> getAccountEmailByUsername(String username);
    Optional<String> getAccountUsernameByEmail(String email);
}
