package id.ac.ui.cs.advprog.accountservice.service;

import id.ac.ui.cs.advprog.accountservice.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
public class FirebaseApiServiceImpl implements FirebaseApiService {
    @Autowired
    private WebClient webClient;

    private final RestTemplate restTemplate;

    @Autowired
    public FirebaseApiServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    private final String API_KEY = "AIzaSyBI5aV5OFsrfn9TuXcCY86WAwYBvQVdUyI";

    public FirebaseApiServiceImpl(WebClient webClient, RestTemplate restTemplate) {
        this.webClient = webClient;
        this.restTemplate = restTemplate;
    }

    @Override
    public FirebaseTokenResponseV2 signupNewUser(FirebaseRegisterRequest requestBody) {
        return webClient.post()
                .uri(builder -> builder
                        .path("/accounts:signUp")
                        .queryParam("key", API_KEY)
                        .build())
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(FirebaseTokenResponseV2.class)
                .timeout(Duration.ofSeconds(5))
                .block();
    }

    @Override
    public FirebaseTokenResponseV2 verifyPassword(FirebaseLoginRequest requestBody) {
        return webClient.post()
                .uri(builder -> builder
                        .path("/accounts:signInWithPassword")
                        .queryParam("key",API_KEY)
                        .build())
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(FirebaseTokenResponseV2.class)
                .timeout(Duration.ofSeconds(5))
                .block();
    }

    @Override
    public FirebaseTokenResponseV2 setAccountInfo(FirebaseUpdateUserDisplayNameRequest requestBody) {
        return webClient.post()
                .uri(builder -> builder
                        .path("/accounts:update")
                        .queryParam("key",API_KEY)
                        .build())
                .body(BodyInserters.fromValue(requestBody)) // Request body payload
                .retrieve()
                .bodyToMono(FirebaseTokenResponseV2.class)
                .timeout(Duration.ofSeconds(5)) // Cancel request if it takes more than 5 secs
                .block();
    }

    @Override
    public FirebaseTokenResponseV1 exchangeToken(RefreshTokenRequest requestBody) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var payload = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity("token?key="+API_KEY
                        , payload
                        , FirebaseTokenResponseV1.class)
                .getBody();
    }

}
