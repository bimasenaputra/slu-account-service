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
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private final Account acc = new Account();

    @BeforeEach
    public void setup() {
        acc.setFirstName("sprint");
        acc.setLastName("satu");
        acc.setEmail("satu@gmail.com");
        acc.setUsername("satusatu");

        accountService.createAccount(acc);
    }

    @Test
    public void createAccountTest() {
        Account account = new Account();
        account.setFirstName("Sprint");
        account.setLastName("Satu");
        account.setEmail("sprintsatu@gmail.com");
        account.setUsername("satu");

        accountService.createAccount(account);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void getAccountByEmailTest() {
        Mockito.when(accountService.getAccountByEmail("satu@gmail.com")).thenReturn(Optional.of(acc));
        var account = accountService.getAccountByEmail("satu@gmail.com");
        verify(accountRepository, times(1)).findAccountByEmail("satu@gmail.com");
        assertEquals(acc, account.orElse(null));
    }

    @Test
    public void getAccountByUsernameTest() {
        Mockito.when(accountService.getAccountByUsername("satusatu")).thenReturn(Optional.of(acc));
        var account = accountService.getAccountByUsername("satusatu");
        verify(accountRepository, times(1)).findAccountByUsername("satusatu");
        assertEquals(acc, account.orElse(null));
    }

    @Test
    public void getAccountUsernameByEmailTest() {
        Mockito.when(accountService.getAccountUsernameByEmail("satu@gmail.com")).thenReturn(Optional.of(acc.getUsername()));
        Mockito.when(accountService.getAccountByEmail("satu@gmail.com")).thenReturn(Optional.of(acc));
        var usernameOpt = accountService.getAccountUsernameByEmail("satu@gmail.com");
        verify(accountRepository, times(1)).findAccountByEmail("satu@gmail.com");
        assertEquals(acc.getUsername(), usernameOpt.orElse("beda"));
    }

    @Test
    public void getAccountEmailByUsernameTest() {
        Mockito.when(accountService.getAccountEmailByUsername("satusatu")).thenReturn(Optional.of(acc.getEmail()));
        Mockito.when(accountService.getAccountByUsername("satusatu")).thenReturn(Optional.of(acc));
        var emailOpt = accountService.getAccountEmailByUsername("satusatu");
        verify(accountRepository, times(1)).findAccountByUsername("satusatu");
        assertEquals(acc.getEmail(), emailOpt.orElse("beda"));
    }
}