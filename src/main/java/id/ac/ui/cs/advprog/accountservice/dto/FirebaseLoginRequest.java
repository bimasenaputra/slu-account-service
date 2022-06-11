package id.ac.ui.cs.advprog.accountservice.dto;

import lombok.Getter;

@Getter
public class FirebaseLoginRequest {
    private String email;
    private String password;
    private boolean returnSecureToken;

    public FirebaseLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.returnSecureToken = true;
    }
}
