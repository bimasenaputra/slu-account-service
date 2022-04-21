package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class EmailExistsStrategy extends ErrorStrategy {
    @Override
    public String getMessage() {
        return "The email address is already in use by another account.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.BAD_REQUEST; }
}
