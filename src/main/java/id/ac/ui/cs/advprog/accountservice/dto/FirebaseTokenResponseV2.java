package id.ac.ui.cs.advprog.accountservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirebaseTokenResponseV2 {
    private String idToken;
    private String refreshToken;

    public String getCookieValue() {
        return "idToken=" + getIdToken()
                + "; refreshToken=" + getRefreshToken()
                + "; Secure; HttpOnly";
    }
}
