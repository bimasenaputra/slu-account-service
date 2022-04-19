package id.ac.ui.cs.advprog.accountservice.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.ac.ui.cs.advprog.accountservice.dto.FirebaseLoginRequest;
import id.ac.ui.cs.advprog.accountservice.service.AccountService;
import io.netty.handler.timeout.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


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
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message","Wrong request body payload");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        var email = accountService.getAccountEmailByUsername(loginForm.get("username").toString());
        if (email.isEmpty()) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message","Account with username " + loginForm.get("username") + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
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
                    .body(BodyInserters.fromValue(requestBody)) // Request body payload
                    .retrieve()
                    .bodyToMono((Class<Map<String,Object>>)(Class)Map.class)
                    .timeout(Duration.ofSeconds(5)) // Cancel request if it takes more than 5 secs
                    .block();
            if (firebaseResponse == null) {
                Map<String, Object> responseBody = new LinkedHashMap<>();
                responseBody.put("message", "Response is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseBody);
            }
            // Move token from body to secure http-only cookie for security reason
            final String cookieValue = "idToken=" + firebaseResponse.get("idToken").toString()
                    + "; refreshToken=" + firebaseResponse.get("refreshToken").toString()
                    + "; Secure; HttpOnly";

            var responseHeader = new HttpHeaders();
            responseHeader.add(HttpHeaders.SET_COOKIE, cookieValue);

            firebaseResponse.remove("idToken");
            firebaseResponse.remove("refreshToken");
            return ResponseEntity.ok().headers(responseHeader).body(firebaseResponse);
        } catch (TimeoutException e) {
            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(responseBody);
        } catch (WebClientResponseException e) {
            Map<String, Object> responseBody = new Gson().fromJson(
                    e.getResponseBodyAsString(), new TypeToken<HashMap<String, Object>>() {}.getType()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }
}
