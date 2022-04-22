package id.ac.ui.cs.advprog.accountservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirebaseRegisterRequest {
    private String email;
    private String password;
    private boolean returnSecureToken;

    public FirebaseRegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.returnSecureToken = true;
    }
}
