package id.ac.ui.cs.advprog.accountservice.repository;

import id.ac.ui.cs.advprog.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);
    Account findByEmail(String email);
}
