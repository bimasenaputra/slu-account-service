package id.ac.ui.cs.advprog.accountservice.controller;

import id.ac.ui.cs.advprog.accountservice.dto.*;
import id.ac.ui.cs.advprog.accountservice.exception.ErrorType;
import id.ac.ui.cs.advprog.accountservice.model.Account;
import id.ac.ui.cs.advprog.accountservice.service.AccountService;
import id.ac.ui.cs.advprog.accountservice.service.FirebaseApiService;
import id.ac.ui.cs.advprog.accountservice.util.CookieExtractor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("auth")
public class AuthController {

    private final FirebaseApiService firebaseApiService;

    private final AccountService accountService;

    public AuthController(FirebaseApiService firebaseApiService, AccountService accountService) {
        this.firebaseApiService = firebaseApiService;
        this.accountService = accountService;
    }

    private void updateUserDisplayName(FirebaseUpdateUserRequest requestBody) {
        var firebaseResponse = firebaseApiService.setAccountInfo(requestBody);
        assert firebaseResponse != null;
    }

    private ResponseEntity<Map<String, String>> resolveRegister(FirebaseRegisterRequest requestBody, Account account) {
        // Call firebase REST API & fetch response payload
        var firebaseResponse = firebaseApiService.signupNewUser(requestBody);
        assert firebaseResponse != null;
        // Call firebase REST API to update user display name
        var updateUserDisplayNameRequestBody = new FirebaseUpdateUserRequest(firebaseResponse.getIdToken(), account.getUsername());
        updateUserDisplayName(updateUserDisplayNameRequestBody);
        // Move token from body to secure http-only cookie for security reason
        final String cookieValue = firebaseResponse.getCookieValue();
        var responseHeader = new HttpHeaders();
        responseHeader.add(HttpHeaders.SET_COOKIE, cookieValue);
        accountService.createAccount(account);
        return ResponseEntity.ok().headers(responseHeader).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterForm registerForm) {
        var existingAccount = accountService.getAccountByUsername(registerForm.getUsername());
        if (existingAccount.isPresent()) throw new IllegalArgumentException(ErrorType.USERNAME_EXISTS.toString());

        var account = new Account(registerForm.getEmail(), registerForm.getUsername(), registerForm.getFirstName(), registerForm.getLastName());
        var requestBody = new FirebaseRegisterRequest(registerForm.getEmail(), registerForm.getPassword());

        return resolveRegister(requestBody, account);
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

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginForm loginForm) {
        var email = accountService.getAccountEmailByUsername(loginForm.getUsername());
        if (email.isEmpty()) throw new IllegalArgumentException(ErrorType.USERNAME_NOT_FOUND.toString());
        var requestBody = new FirebaseLoginRequest(email.get(), loginForm.getPassword());
        return resolveLogin(requestBody);
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
