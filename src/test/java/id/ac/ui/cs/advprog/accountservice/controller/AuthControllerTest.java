package id.ac.ui.cs.advprog.accountservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.accountservice.dto.*;
import id.ac.ui.cs.advprog.accountservice.model.Account;
import id.ac.ui.cs.advprog.accountservice.service.AccountService;
import id.ac.ui.cs.advprog.accountservice.service.FirebaseApiService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private FirebaseApiService firebaseApiService;


    @Test
    public void register_success() throws Exception {
        var form = new RegisterForm();
        form.setEmail("test@test.test");
        form.setFirstName("test");
        form.setLastName("test");
        form.setPassword("test");
        form.setUsername("test");
        Mockito.when(accountService.getAccountByUsername("test")).thenReturn(Optional.empty());

        var requestMock1 = new FirebaseRegisterRequest("test","test");
        var responseMock = new FirebaseTokenResponseV2();
        responseMock.setIdToken("123");
        responseMock.setRefreshToken("456");
        Mockito.when(firebaseApiService.signupNewUser(requestMock1)).thenReturn(responseMock);

        var requestMock2 = new FirebaseUpdateUserDisplayNameRequest("123","test");
        Mockito.when(firebaseApiService.setAccountInfo(requestMock2)).thenReturn(responseMock);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(form));

        var account = new Account();
        account.setEmail("test@test.test");
        account.setFirstName("test");
        account.setLastName("test");
        account.setUsername("test");

        verify(accountService).createAccount(account);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(header().string( "Set-Cookie",responseMock.getCookieValue()));
    }

    @Test
    public void register_fail() throws Exception {
        var form = new RegisterForm();
        form.setEmail("test@test.test");
        form.setFirstName("test");
        form.setLastName("test");
        form.setPassword("test");
        form.setUsername("test");

        var account = new Account("test@test.test","test","test","test");

        Mockito.when(accountService.getAccountByUsername("test")).thenReturn(Optional.of(account));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(form));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", is("The email address is already in use by another account.")));
    }

    @Test
    public void login_success() throws Exception {
        var form = new LoginForm();
        form.setUsername("test");
        form.setPassword("test");

        var account = new Account("test@test.test","test","test","test");

        Mockito.when(accountService.getAccountEmailByUsername(account.getUsername())).thenReturn(Optional.of(account.getEmail()));

        var requestMock = new FirebaseLoginRequest(account.getEmail(),form.getPassword());
        var responseMock = new FirebaseTokenResponseV2();
        responseMock.setIdToken("123");
        responseMock.setRefreshToken("456");
        Mockito.when(firebaseApiService.verifyPassword(requestMock)).thenReturn(responseMock);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(form));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(header().string( "Set-Cookie",responseMock.getCookieValue()));
    }


}
