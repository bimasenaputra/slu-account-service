package id.ac.ui.cs.advprog.accountservice.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String grant_type;
    private String refresh_token;

    public RefreshTokenRequest(String refresh_token) {
        this.refresh_token = refresh_token;
        this.grant_type = "refresh_token";
    }
}
