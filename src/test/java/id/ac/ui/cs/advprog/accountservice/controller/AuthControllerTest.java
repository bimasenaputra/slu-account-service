package id.ac.ui.cs.advprog.accountservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private FirebaseApiService firebaseApiService;

    @Test
    void register_fail() throws Exception {
        var form = new RegisterForm();
        var strMock = Mockito.anyString();
        form.setEmail(strMock);
        form.setFirstName(strMock);
        form.setLastName(strMock);
        form.setPassword(strMock);
        form.setUsername(strMock);

        var accountMock = new Account(strMock,strMock,strMock,strMock);

        Mockito.when(accountService.getAccountByUsername(strMock)).thenReturn(Optional.of(accountMock));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(form));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", is("The username is already in use by another account.")));
    }

    @Test
    void login_fail() throws Exception {
        var form = new LoginForm();
        var strMock = Mockito.anyString();
        form.setUsername(strMock);
        form.setPassword(strMock);

        Mockito.when(accountService.getAccountEmailByUsername(strMock)).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(form));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", is("There is no user record corresponding to this username.")));
    }

    @Test
    void refresh_fail() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(HttpHeaders.EMPTY);
        mockMvc.perform(mockRequest)
                .andExpect(status().is5xxServerError());
    }
}
