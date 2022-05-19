package id.ac.ui.cs.advprog.accountservice.service;

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

public class FirebaseApiServiceImplTest {
    private ObjectMapper mapper;

    private static MockWebServer mockWebServer;
    private static FirebaseApiService firebaseApiService;

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
    public void signupNewUserTest_success() throws Exception {
        var responseMock = new FirebaseTokenResponseV2();
        responseMock.setIdToken("123");
        responseMock.setRefreshToken("456");

        var requestMock = new FirebaseRegisterRequest("tanjirokamado@com.gmail","juichinokata");

        mockWebServer.enqueue(new MockResponse().setBody(this.mapper.writeValueAsString(responseMock)).addHeader("Content-Type", "application/json"));

        var actualResponse = firebaseApiService.signupNewUser(requestMock);

        assertEquals(responseMock.getIdToken(), actualResponse.getIdToken());
        assertEquals(responseMock.getRefreshToken(), actualResponse.getRefreshToken());
    }

    @Test
    public void signupNewUserTest_fail() {
        var responseMock = new FirebaseTokenResponseV2();
        responseMock.setIdToken("123");
        responseMock.setRefreshToken("456");

        var requestMock = new FirebaseRegisterRequest("tanjiro@gmail.com","PostgreSQL");

        mockWebServer.enqueue(new MockResponse().setResponseCode(400).addHeader("Content-Type", "application/json"));

        assertThrows(WebClientResponseException.class, () -> firebaseApiService.signupNewUser(requestMock));
    }

    @Test
    public void verifyPasswordTest_success() throws Exception {
        var responseMock = new FirebaseTokenResponseV2();
        responseMock.setIdToken("123");
        responseMock.setRefreshToken("456");

        var requestMock = new FirebaseLoginRequest("tanjirokamado@com.gmail","juichinokata");

        mockWebServer.enqueue(new MockResponse().setBody(this.mapper.writeValueAsString(responseMock)).addHeader("Content-Type", "application/json"));

        var actualResponse = firebaseApiService.verifyPassword(requestMock);

        assertEquals(responseMock.getIdToken(), actualResponse.getIdToken());
        assertEquals(responseMock.getRefreshToken(), actualResponse.getRefreshToken());
    }

    @Test
    public void verifyPasswordTest_fail() {
        var requestMock = new FirebaseLoginRequest("tanjirokamado@com.gmail","矿泉水");

        mockWebServer.enqueue(new MockResponse().setResponseCode(400).addHeader("Content-Type", "application/json"));

        assertThrows(WebClientResponseException.class, () -> firebaseApiService.verifyPassword(requestMock));
    }

    @Test
    public void setAccountInfoTest() throws Exception {
        var responseMock = new FirebaseTokenResponseV2();
        responseMock.setIdToken("123");
        responseMock.setRefreshToken("456");

        var requestMock = new FirebaseUpdateUserDisplayNameRequest("123","Anonymous");

        mockWebServer.enqueue(new MockResponse().setBody(this.mapper.writeValueAsString(responseMock)).addHeader("Content-Type", "application/json"));

        var actualResponse = firebaseApiService.setAccountInfo(requestMock);

        assertEquals(responseMock.getIdToken(), actualResponse.getIdToken());
        assertEquals(responseMock.getRefreshToken(), actualResponse.getRefreshToken());
    }

    @Test
    public void exchangeTokenTest() throws Exception {
        var responseMock = new FirebaseTokenResponseV1();
        responseMock.setId_token("123");
        responseMock.setRefresh_token("456");

        var requestMock = new RefreshTokenRequest("789");

        mockWebServer.enqueue(new MockResponse().setBody(this.mapper.writeValueAsString(responseMock)).addHeader("Content-Type", "application/json"));

        var actualResponse = firebaseApiService.exchangeToken(requestMock);

        assertEquals(responseMock.getId_token(), actualResponse.getId_token());
        assertEquals(responseMock.getRefresh_token(), actualResponse.getRefresh_token());
    }
}
