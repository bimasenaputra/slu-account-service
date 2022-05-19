package id.ac.ui.cs.advprog.accountservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountIdTest {
    @Test
    public void no_arg() {
        var id = new AccountId();
        assertNotNull(id);
    }

    @Test
    public void all_arg() {
        var id = new AccountId("a@gmail.com","a");
        assertNotNull(id);
    }
}
