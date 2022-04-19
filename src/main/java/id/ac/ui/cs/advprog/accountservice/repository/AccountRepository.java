package id.ac.ui.cs.advprog.accountservice.repository;

import id.ac.ui.cs.advprog.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findAccountByEmail(String email);
    Optional<Account> findAccountByUsername(String username);
}
