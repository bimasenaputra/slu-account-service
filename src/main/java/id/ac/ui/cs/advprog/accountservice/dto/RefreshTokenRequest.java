package id.ac.ui.cs.advprog.accountservice.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String grantType;
    private String refreshToken;

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
        this.grantType = "refresh_token";
    }
}
