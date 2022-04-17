package id.ac.ui.cs.advprog.accountservice.service;

import id.ac.ui.cs.advprog.accountservice.model.Account;

import java.util.List;

public interface AccountService {
    Account createUser(Account user);
    Account getUser(String username);
    Account getUserByEmail(String email);
    List<Account> getAllUsers();
}
