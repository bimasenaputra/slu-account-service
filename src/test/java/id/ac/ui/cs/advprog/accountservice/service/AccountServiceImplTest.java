package id.ac.ui.cs.advprog.accountservice.service;

import id.ac.ui.cs.advprog.accountservice.model.Account;
import id.ac.ui.cs.advprog.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private static final String firstName = "sprint";
    private static final String lastName = "satu";
    private static final String email = "satu@gmail.com";
    private static final String username= "satusatu";

    private final Account acc = new Account();

    @BeforeEach
    void setup() {
        acc.setFirstName(firstName);
        acc.setLastName(lastName);
        acc.setEmail(email);
        acc.setUsername(username);

        accountService.createAccount(acc);
    }

    @Test
    void createAccountTest() {
        Account account = new Account();
        account.setFirstName("Sprint");
        account.setLastName("Satu");
        account.setEmail("sprintsatu@gmail.com");
        account.setUsername("satu");

        accountService.createAccount(account);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void getAccountByEmailTest() {
        Mockito.when(accountService.getAccountByEmail(email)).thenReturn(Optional.of(acc));
        var account = accountService.getAccountByEmail(email);
        verify(accountRepository, times(1)).findAccountByEmail(email);
        assertEquals(acc, account.orElse(null));
    }

    @Test
    void getAccountByUsernameTest() {
        Mockito.when(accountService.getAccountByUsername(username)).thenReturn(Optional.of(acc));
        var account = accountService.getAccountByUsername(username);
        verify(accountRepository, times(1)).findAccountByUsername(username);
        assertEquals(acc, account.orElse(null));
    }

    @Test
    void getAccountUsernameByEmailTest() {
        Mockito.when(accountService.getAccountUsernameByEmail(email)).thenReturn(Optional.of(acc.getUsername()));
        Mockito.when(accountService.getAccountByEmail(email)).thenReturn(Optional.of(acc));
        var usernameOpt = accountService.getAccountUsernameByEmail(email);
        verify(accountRepository, times(1)).findAccountByEmail(email);
        assertEquals(acc.getUsername(), usernameOpt.orElse(email+"b"));
    }

    @Test
    void getAccountEmailByUsernameTest() {
        Mockito.when(accountService.getAccountEmailByUsername(username)).thenReturn(Optional.of(acc.getEmail()));
        Mockito.when(accountService.getAccountByUsername(username)).thenReturn(Optional.of(acc));
        var emailOpt = accountService.getAccountEmailByUsername(username);
        verify(accountRepository, times(1)).findAccountByUsername(username);
        assertEquals(acc.getEmail(), emailOpt.orElse(email+"a"));
    }
}