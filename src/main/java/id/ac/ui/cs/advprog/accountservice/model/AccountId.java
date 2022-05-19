package id.ac.ui.cs.advprog.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class AccountId implements Serializable {
    private String email;
    private String username;
}
