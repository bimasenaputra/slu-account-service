package id.ac.ui.cs.advprog.accountservice.controller;

import com.google.gson.Gson;
import id.ac.ui.cs.advprog.accountservice.dto.*;
import id.ac.ui.cs.advprog.accountservice.exception.ErrorType;
import id.ac.ui.cs.advprog.accountservice.model.Account;
import id.ac.ui.cs.advprog.accountservice.service.AccountService;
import io.netty.handler.timeout.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import static id.ac.ui.cs.advprog.accountservice.util.ErrorHandler.resolveError;


@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private WebClient webClient;

    @Autowired
    private AccountService accountService;

    private final String API_KEY = "AIzaSyBI5aV5OFsrfn9TuXcCY86WAwYBvQVdUyI";

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> registerForm) {
        if (!(registerForm.containsKey("email") && registerForm.containsKey("username") && registerForm.containsKey("password"))) {
            return resolveError(ErrorType.WRONG_REQUEST_PAYLOAD);
        }

        var email = registerForm.get("email").toString();
        var username = registerForm.get("username").toString();
        var password = registerForm.get("password").toString();

        var account = new Account(email, username);

        var requestBody = new FirebaseRegisterRequest(email, password);

        return resolveRegister(requestBody, account);
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<Map<String, Object>> resolveRegister(FirebaseRegisterRequest requestBody, Account account) {
        try {
            // Call firebase REST API & fetch response payload
            var firebaseResponse = webClient.post()
                    .uri(builder -> builder
                            .path("/accounts:signUp")
                            .queryParam("key",API_KEY)
                            .build())
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono((Class<Map<String,Object>>)(Class)Map.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            assert firebaseResponse != null;

            // Call firebase REST API to update user display name
            var updateUserDisplayNameRequestBody = new FirebaseUpdateUserDisplayNameRequest(firebaseResponse.get("idToken").toString(), account.getUsername());
            updateUserDisplayName(updateUserDisplayNameRequestBody);

            // Move token from body to secure http-only cookie for security reason
            final String cookieValue = "idToken=" + firebaseResponse.get("idToken").toString()
                    + "; refreshToken=" + firebaseResponse.get("refreshToken").toString()
                    + "; Secure; HttpOnly";

            var responseHeader = new HttpHeaders();
            responseHeader.add(HttpHeaders.SET_COOKIE, cookieValue);

            firebaseResponse.remove("idToken");
            firebaseResponse.remove("refreshToken");
            firebaseResponse.remove("localId");

            accountService.createAccount(account);

            return ResponseEntity.ok().headers(responseHeader).body(firebaseResponse);
        } catch (WebClientResponseException e) {
            FirebaseError error = new Gson().fromJson(e.getResponseBodyAsString(), FirebaseError.class);
            FirebaseErrorResponse responseBody = error.getError();
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", responseBody.parseMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (TimeoutException e) {
            System.out.println("Timeout");
            return resolveError(ErrorType.TIMEOUT);
        } catch (AssertionError e) {
            System.out.println("AssertError");
            return resolveError(ErrorType.SERVER_ERROR);
        } catch (Exception e) {
            System.out.println("Exception idk");
            return resolveError(ErrorType.UNKNOWN_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    private void updateUserDisplayName(FirebaseUpdateUserDisplayNameRequest requestBody) throws TimeoutException, WebClientResponseException, AssertionError{
        var firebaseResponse = webClient.post()
                .uri(builder -> builder
                        .path("/accounts:update")
                        .queryParam("key",API_KEY)
                        .build())
                .body(BodyInserters.fromValue(requestBody)) // Request body payload
                .retrieve()
                .bodyToMono((Class<Map<String,Object>>)(Class)Map.class)
                .timeout(Duration.ofSeconds(5)) // Cancel request if it takes more than 5 secs
                .block();

        assert firebaseResponse != null;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> loginForm) {
        if (!(loginForm.containsKey("username") && loginForm.containsKey("password"))) {
            return resolveError(ErrorType.WRONG_REQUEST_PAYLOAD);
        }
        var email = accountService.getAccountEmailByUsername(loginForm.get("username").toString());
        if (email.isEmpty()) return resolveError(ErrorType.INVALID_EMAIL);
        var password = loginForm.get("password").toString();
        var requestBody = new FirebaseLoginRequest(email.get(), password);
        return resolveLogin(requestBody);
    }

    // TODO: Delete this method if register is implemented
    @PostMapping("/login/test")
    public ResponseEntity<Map<String, Object>> testLogin(@RequestBody Map<String, Object> loginForm) {
        var requestBody = new FirebaseLoginRequest(loginForm.get("username").toString(), loginForm.get("password").toString());
        return resolveLogin(requestBody);
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<Map<String, Object>> resolveLogin(FirebaseLoginRequest requestBody) {
        try {
            // Call firebase REST API & fetch response payload
            var firebaseResponse = webClient.post()
                    .uri(builder -> builder
                            .path("/accounts:signInWithPassword")
                            .queryParam("key",API_KEY)
                            .build())
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono((Class<Map<String,Object>>)(Class)Map.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            assert firebaseResponse != null;

            // Move token from body to secure http-only cookie for security reason
            final String cookieValue = "idToken=" + firebaseResponse.get("idToken").toString()
                    + "; refreshToken=" + firebaseResponse.get("refreshToken").toString()
                    + "; Secure; HttpOnly";

            var responseHeader = new HttpHeaders();
            responseHeader.add(HttpHeaders.SET_COOKIE, cookieValue);

            firebaseResponse.remove("idToken");
            firebaseResponse.remove("refreshToken");
            firebaseResponse.remove("localId");

            return ResponseEntity.ok().headers(responseHeader).body(firebaseResponse);
        } catch (WebClientResponseException e) {
            FirebaseError error = new Gson().fromJson(e.getResponseBodyAsString(), FirebaseError.class);
            FirebaseErrorResponse responseBody = error.getError();
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", responseBody.parseMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (TimeoutException e) {
            System.out.println("Timeout");
            return resolveError(ErrorType.TIMEOUT);
        } catch (AssertionError e) {
            System.out.println("AssertError");
            return resolveError(ErrorType.SERVER_ERROR);
        } catch (Exception e) {
            System.out.println("Exception idk");
            return resolveError(ErrorType.UNKNOWN_ERROR);
        }
    }
}
