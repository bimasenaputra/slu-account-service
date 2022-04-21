package id.ac.ui.cs.advprog.accountservice.controller;

import id.ac.ui.cs.advprog.accountservice.dto.RefreshTokenRequest;
import id.ac.ui.cs.advprog.accountservice.exception.ErrorType;
import id.ac.ui.cs.advprog.accountservice.util.CookieExtractor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static id.ac.ui.cs.advprog.accountservice.util.ErrorHandler.resolveError;

@RestController
public class ReAuthController {

    private final String API_KEY = "AIzaSyBI5aV5OFsrfn9TuXcCY86WAwYBvQVdUyI";

    @SuppressWarnings("unchecked")
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(@RequestHeader(name= HttpHeaders.COOKIE) String cookie) {
        if (cookie == null || !cookie.contains("refreshToken")) {
            return resolveError(ErrorType.WRONG_REQUEST_PAYLOAD);
        }
        var refreshToken = CookieExtractor.extract(cookie).get("refreshToken");

        var restTemplate = new RestTemplate();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var requestBody = new RefreshTokenRequest(refreshToken);
        var payload = new HttpEntity<>(requestBody, headers);
        Map<String, String> params = new HashMap<>();
        params.put("key",API_KEY);

        try {
            var firebaseResponse = restTemplate.postForEntity("https://securetoken.googleapis.com/v1/"
                    , payload
                    , (Class<Map<String,Object>>)(Class)Map.class,params)
                    .getBody();

            assert firebaseResponse != null;

            // Move token to secure http-only cookie for security reason
            final String cookieValue = "idToken=" + firebaseResponse.get("id_token").toString()
                    + "; refreshToken=" + firebaseResponse.get("refresh_token").toString()
                    + "; Secure; HttpOnly";
            var responseHeader = new HttpHeaders();
            responseHeader.add(HttpHeaders.SET_COOKIE, cookieValue);

            firebaseResponse.remove("id_token");
            firebaseResponse.remove("refresh_token");
            firebaseResponse.remove("user_id");
            firebaseResponse.remove("project_id");

            return ResponseEntity.ok().headers(responseHeader).body(firebaseResponse);
        } catch (RestClientException e) {
            return resolveError(ErrorType.INVALID_REFRESH_TOKEN);
        } catch (AssertionError e) {
            return resolveError(ErrorType.SERVER_ERROR);
        } catch (Exception e) {
            return resolveError(ErrorType.UNKNOWN_ERROR);
        }
    }

}
