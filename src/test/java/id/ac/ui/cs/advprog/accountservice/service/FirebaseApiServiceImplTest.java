package id.ac.ui.cs.advprog.accountservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.accountservice.dto.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FirebaseApiServiceImplTest {
    private ObjectMapper mapper;

    private static MockWebServer mockWebServer;
    private FirebaseApiService firebaseApiService;

    private static final String email = "satu@gmail.com";
    private static final String headerName = "Content-Type";
    private static final String headerValue = "application/json";

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void initialize() {
        mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(mockWebServer.url("/").toString()));
        firebaseApiService = new FirebaseApiServiceImpl(WebClient.create(mockWebServer.url("/").toString()), restTemplate);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void signupNewUserTest_success() throws JsonProcessingException {
        var responseMock = new FirebaseTokenResponseV2();
        responseMock.setIdToken("123");
        responseMock.setRefreshToken("456");

        var requestMock = new FirebaseRegisterRequest(email,"juichinokata");

        mockWebServer.enqueue(new MockResponse().setBody(this.mapper.writeValueAsString(responseMock)).addHeader(headerName, headerValue));

        var actualResponse = firebaseApiService.signupNewUser(requestMock);

        assertEquals(responseMock.getIdToken(), actualResponse.getIdToken());
        assertEquals(responseMock.getRefreshToken(), actualResponse.getRefreshToken());
    }

    @Test
    void signupNewUserTest_fail() {
        var responseMock = new FirebaseTokenResponseV2();
        responseMock.setIdToken("123");
        responseMock.setRefreshToken("456");

        var requestMock = new FirebaseRegisterRequest("dua@gmail.com","PostgreSQL");

        mockWebServer.enqueue(new MockResponse().setResponseCode(400).addHeader(headerName, headerValue));

        assertThrows(WebClientResponseException.class, () -> firebaseApiService.signupNewUser(requestMock));
    }

    @Test
    void verifyPasswordTest_success() throws JsonProcessingException {
        var responseMock = new FirebaseTokenResponseV2();
        responseMock.setIdToken("123");
        responseMock.setRefreshToken("456");

        var requestMock = new FirebaseLoginRequest(email,"juichinokata");

        mockWebServer.enqueue(new MockResponse().setBody(this.mapper.writeValueAsString(responseMock)).addHeader(headerName, headerValue));

        var actualResponse = firebaseApiService.verifyPassword(requestMock);

        assertEquals(responseMock.getIdToken(), actualResponse.getIdToken());
        assertEquals(responseMock.getRefreshToken(), actualResponse.getRefreshToken());
    }

    @Test
    void verifyPasswordTest_fail() {
        var requestMock = new FirebaseLoginRequest(email,"矿泉水");

        mockWebServer.enqueue(new MockResponse().setResponseCode(400).addHeader(headerName, headerValue));

        assertThrows(WebClientResponseException.class, () -> firebaseApiService.verifyPassword(requestMock));
    }

    @Test
    void setAccountInfoTest() throws JsonProcessingException {
        var responseMock = new FirebaseTokenResponseV2();
        responseMock.setIdToken("123");
        responseMock.setRefreshToken("456");

        var requestMock = new FirebaseUpdateUserRequest("123","Anonymous");

        mockWebServer.enqueue(new MockResponse().setBody(this.mapper.writeValueAsString(responseMock)).addHeader(headerName, headerValue));

        var actualResponse = firebaseApiService.setAccountInfo(requestMock);

        assertEquals(responseMock.getIdToken(), actualResponse.getIdToken());
        assertEquals(responseMock.getRefreshToken(), actualResponse.getRefreshToken());
    }

    @Test
    void exchangeTokenTest() throws JsonProcessingException {
        var responseMock = new FirebaseTokenResponseV1();
        responseMock.setId_token("123");
        responseMock.setRefresh_token("456");

        var requestMock = new RefreshTokenRequest("789");

        mockWebServer.enqueue(new MockResponse().setBody(this.mapper.writeValueAsString(responseMock)).addHeader(headerName, headerValue));

        var actualResponse = firebaseApiService.exchangeToken(requestMock);

        assertEquals(responseMock.getId_token(), actualResponse.getId_token());
        assertEquals(responseMock.getRefresh_token(), actualResponse.getRefresh_token());
    }
}
