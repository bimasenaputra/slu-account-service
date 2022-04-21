package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class UserDisabledStrategy extends ErrorStrategy {
    @Override
    public String getMessage() {
        return "The user account has been disabled.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.FORBIDDEN; }
}
