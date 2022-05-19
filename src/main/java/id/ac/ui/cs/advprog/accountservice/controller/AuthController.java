package id.ac.ui.cs.advprog.accountservice.controller;

import id.ac.ui.cs.advprog.accountservice.dto.*;
import id.ac.ui.cs.advprog.accountservice.exception.ErrorType;
import id.ac.ui.cs.advprog.accountservice.model.Account;
import id.ac.ui.cs.advprog.accountservice.service.AccountService;
import id.ac.ui.cs.advprog.accountservice.service.FirebaseApiServiceImpl;
import id.ac.ui.cs.advprog.accountservice.util.CookieExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private FirebaseApiServiceImpl firebaseApiService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterForm registerForm) {
        var firstName = registerForm.getFirstName();
        var lastName = registerForm.getLastName();
        var email = registerForm.getEmail();
        var username = registerForm.getUsername();
        var password = registerForm.getPassword();

        var existingAccount = accountService.getAccountByUsername(username);
        if (existingAccount.isPresent()) throw new RuntimeException(ErrorType.USERNAME_EXISTS.toString());

        var account = new Account(email, username, firstName, lastName);

        var requestBody = new FirebaseRegisterRequest(email, password);

        return resolveRegister(requestBody, account);
    }

    private ResponseEntity<Map<String, String>> resolveRegister(FirebaseRegisterRequest requestBody, Account account) {
            // Call firebase REST API & fetch response payload
            var firebaseResponse = firebaseApiService.signupNewUser(requestBody);

            assert firebaseResponse != null;

            // Call firebase REST API to update user display name
            var updateUserDisplayNameRequestBody = new FirebaseUpdateUserDisplayNameRequest(firebaseResponse.getIdToken(), account.getUsername());
            updateUserDisplayName(updateUserDisplayNameRequestBody);

            // Move token from body to secure http-only cookie for security reason
            final String cookieValue = firebaseResponse.getCookieValue();

            var responseHeader = new HttpHeaders();
            responseHeader.add(HttpHeaders.SET_COOKIE, cookieValue);

            accountService.createAccount(account);

            return ResponseEntity.ok().headers(responseHeader).build();
    }

    private void updateUserDisplayName(FirebaseUpdateUserDisplayNameRequest requestBody) {
        var firebaseResponse = firebaseApiService.setAccountInfo(requestBody);
        assert firebaseResponse != null;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginForm loginForm) {
        var email = accountService.getAccountEmailByUsername(loginForm.getUsername());
        if (email.isEmpty()) throw new RuntimeException(ErrorType.USERNAME_NOT_FOUND.toString());
        var password = loginForm.getPassword();
        var requestBody = new FirebaseLoginRequest(email.get(), password);
        return resolveLogin(requestBody);
    }

    private ResponseEntity<Map<String, String>> resolveLogin(FirebaseLoginRequest requestBody) {
            // Call firebase REST API & fetch response payload
            var firebaseResponse = firebaseApiService.verifyPassword(requestBody);

            assert firebaseResponse != null;

            // Move token from body to secure http-only cookie for security reason
            final String cookieValue = firebaseResponse.getCookieValue();

            var responseHeader = new HttpHeaders();
            responseHeader.add(HttpHeaders.SET_COOKIE, cookieValue);

            return ResponseEntity.ok().headers(responseHeader).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestHeader(name= HttpHeaders.COOKIE) String cookie) {
        if (cookie == null || !cookie.contains("refreshToken")) {
            throw new AssertionError();
        }

        var refreshToken = CookieExtractor.extract(cookie).get("refreshToken");

        var requestBody = new RefreshTokenRequest(refreshToken);

        var firebaseResponse = firebaseApiService.exchangeToken(requestBody);

        assert firebaseResponse != null;

        // Move token to secure http-only cookie for security reason
        final String cookieValue = firebaseResponse.getCookieValue();

        var responseHeader = new HttpHeaders();
        responseHeader.add(HttpHeaders.SET_COOKIE, cookieValue);

        return ResponseEntity.ok().headers(responseHeader).build();
    }
}
