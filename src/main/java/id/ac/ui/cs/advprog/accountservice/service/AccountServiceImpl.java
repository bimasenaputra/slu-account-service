package id.ac.ui.cs.advprog.accountservice.service;

import id.ac.ui.cs.advprog.accountservice.model.Account;
import id.ac.ui.cs.advprog.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository userAccRepository;

    @Override
    public Account createUser(Account user) {
        return userAccRepository.save(user);
    }

    @Override
    public Account getUser(String username) {
        return userAccRepository.findByUsername(username);
    }

    @Override
    public Account getUserByEmail(String email) {
        return userAccRepository.findByEmail(email);
    }

    @Override
    public List<Account> getAllUsers() {
        return userAccRepository.findAll();
    }
}
