package id.ac.ui.cs.advprog.accountservice.controller;

import com.google.gson.Gson;
import id.ac.ui.cs.advprog.accountservice.dto.FirebaseError;
import id.ac.ui.cs.advprog.accountservice.dto.FirebaseErrorResponse;
import id.ac.ui.cs.advprog.accountservice.dto.FirebaseLoginRequest;
import id.ac.ui.cs.advprog.accountservice.exception.ErrorType;
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
        /*
        * TODO:
        * 1. Create and save account to H2 database
        * 2. Call firebase REST API to register / sign up new account
        * 3. Set account display name with username by calling firebase REST API's update profile
        * 4. Handle all possible exception
        * 5. Use blocking web client / rest template to get firebase REST API response
        * */
        return null;
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
