package id.ac.ui.cs.advprog.accountservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class FirebaseUpdateUserRequest {
    private String idToken;
    private String displayName;
    private String photoUrl;
    private boolean returnSecureToken;

    public FirebaseUpdateUserRequest(String idToken, String displayName) {
        this.idToken = idToken;
        this.displayName = displayName;
        this.photoUrl = null;
        this.returnSecureToken = true;
    }
}
