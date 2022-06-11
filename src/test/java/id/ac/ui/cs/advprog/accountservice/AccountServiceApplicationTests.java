package id.ac.ui.cs.advprog.accountservice;

import id.ac.ui.cs.advprog.accountservice.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceApplicationTests {

    @Autowired
    private AccountService accountService;

    @Test
    void contextLoads() {
        assertNotNull(accountService);
    }
}
