package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidIdTokenStrategy extends ErrorStrategy {
    @Override
    public String getMessage() {
        return "The user's credential is no longer valid. The user must sign in again.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.FORBIDDEN; }
}
