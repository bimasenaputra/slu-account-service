package id.ac.ui.cs.advprog.accountservice.service;

import id.ac.ui.cs.advprog.accountservice.dto.*;

public interface FirebaseApiService {
    FirebaseTokenResponseV2 signupNewUser(FirebaseRegisterRequest requestBody);
    FirebaseTokenResponseV2 verifyPassword(FirebaseLoginRequest requestBody);
    FirebaseTokenResponseV2 setAccountInfo(FirebaseUpdateUserRequest requestBody);
    FirebaseTokenResponseV1 exchangeToken(RefreshTokenRequest requestBody);
}
