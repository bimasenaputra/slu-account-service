package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class UsernameNotFoundStrategy extends ErrorStrategy {
    @Override
    public String getMessage() {
        return "There is no user record corresponding to this username.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.BAD_REQUEST; }

    @Override
    public ErrorType getErrorType() { return ErrorType.USERNAME_NOT_FOUND; }
}
