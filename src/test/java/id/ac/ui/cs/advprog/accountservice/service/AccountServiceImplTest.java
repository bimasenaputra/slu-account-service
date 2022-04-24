package id.ac.ui.cs.advprog.accountservice.service;

import id.ac.ui.cs.advprog.accountservice.model.Account;
import id.ac.ui.cs.advprog.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account acc = new Account();

    @BeforeEach
    public void setup() {
        acc.setFirstName("sprint");
        acc.setLastName("satu");
        acc.setEmail("satu@gmail.com");
        acc.setUsername("satusatu");

        accountService.createAccount(acc);
    }

    @Test
    public void createAccountTest() throws Exception {
        Account account = new Account();
        account.setFirstName("Sprint");
        account.setLastName("Satu");
        account.setEmail("sprintsatu@gmail.com");
        account.setUsername("satu");

        accountService.createAccount(account);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void getAccountByEmailTest() throws Exception {
        lenient().when(accountService.getAccountByEmail("satu@gmail.com")).thenReturn(Optional.ofNullable(acc));
        var account = accountService.getAccountByEmail("satu@gmail.com");
        verify(accountRepository, times(1)).findAccountByEmail("satu@gmail.com");
    }

    @Test
    public void getAccountByUsernameTest() throws Exception {
        lenient().when(accountService.getAccountByUsername("satusatu")).thenReturn(Optional.ofNullable(acc));
        var account = accountService.getAccountByUsername("satusatu");
        verify(accountRepository, times(1)).findAccountByUsername("satusatu");
    }

    @Test
    public void getAccountUsernameByEmailTest() throws Exception {
        var username = accountService.getAccountUsernameByEmail("satu@gmail.com");
        verify(accountRepository, times(1)).findAccountByEmail("satu@gmail.com");
    }

    @Test
    public void getAccountEmailByUsernameTest() throws Exception {
        var email = accountService.getAccountEmailByUsername("satusatu");
        verify(accountRepository, times(1)).findAccountByUsername("satusatu");
    }
}