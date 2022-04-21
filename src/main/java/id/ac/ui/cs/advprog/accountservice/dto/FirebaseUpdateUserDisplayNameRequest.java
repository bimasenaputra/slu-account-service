package id.ac.ui.cs.advprog.accountservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirebaseUpdateUserDisplayNameRequest {
    private String idToken;
    private String displayName;
    private String photoUrl;
    private boolean returnSecureToken;

    public FirebaseUpdateUserDisplayNameRequest(String idToken, String displayName) {
        this.idToken = idToken;
        this.displayName = displayName;
        this.photoUrl = null;
        this.returnSecureToken = true;
    }
}
