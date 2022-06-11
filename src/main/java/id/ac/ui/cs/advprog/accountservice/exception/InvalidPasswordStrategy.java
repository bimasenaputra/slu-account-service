package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordStrategy implements ErrorStrategy {
    @Override
    public String getMessage() {
        return "The password is invalid.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.BAD_REQUEST; }

    @Override
    public ErrorType getErrorType() { return ErrorType.INVALID_PASSWORD; }
}
