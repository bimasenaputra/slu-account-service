package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class WeakPasswordStrategy extends ErrorStrategy {
    @Override
    public String getMessage() {
        return "The password must be 6 characters long or more.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.BAD_REQUEST; }
}
