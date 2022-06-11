package id.ac.ui.cs.advprog.accountservice.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AccountTest {

    private Account acc = new Account("a@gmail.com","a","a","a");

    @Test
    void all_arg() {
        assertNotNull(acc);
    }

    @Test
    void firstname_not_null() {
        assertNotNull(acc.getFirstName());
    }

    @Test
    void lastname_not_null() {
        assertNotNull(acc.getLastName());
    }
}
