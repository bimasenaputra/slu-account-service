package id.ac.ui.cs.advprog.accountservice.service;

import id.ac.ui.cs.advprog.accountservice.model.Account;
import id.ac.ui.cs.advprog.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Optional<String> getAccountEmailByUsername(String username) {
        var account = getAccountByUsername(username);
        var email = account.map(Account::getEmail).orElse(null);
        return Optional.ofNullable(email);
    }

    @Override
    public Optional<String> getAccountUsernameByEmail(String email) {
        var account = getAccountByEmail(email);
        var username = account.map(Account::getUsername).orElse(null);
        return Optional.ofNullable(username);
    }


}
