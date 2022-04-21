package id.ac.ui.cs.advprog.accountservice.model;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class AccountId implements Serializable {
    private String email;
    private String username;
}
