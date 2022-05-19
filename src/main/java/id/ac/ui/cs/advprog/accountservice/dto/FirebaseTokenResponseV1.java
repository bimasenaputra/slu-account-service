package id.ac.ui.cs.advprog.accountservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirebaseTokenResponseV1 {
    private String id_token;
    private String refresh_token;

    public String getCookieValue() {
        return "idToken=" + getId_token()
                + "; refreshToken=" + getRefresh_token()
                + "; Secure; HttpOnly";
    }
}
