package id.ac.ui.cs.advprog.accountservice.dto;

import lombok.Getter;

@Getter
public class FirebaseErrorDetail {
    private String domain;
    private String reason;
    private String message;
}
