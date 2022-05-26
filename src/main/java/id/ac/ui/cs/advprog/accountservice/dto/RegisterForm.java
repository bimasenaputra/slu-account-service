package id.ac.ui.cs.advprog.accountservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}
