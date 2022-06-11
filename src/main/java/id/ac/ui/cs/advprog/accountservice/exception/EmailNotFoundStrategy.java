package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class EmailNotFoundStrategy implements ErrorStrategy {
    @Override
    public String getMessage() {
        return "There is no user record corresponding to this email.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.BAD_REQUEST; }

    @Override
    public ErrorType getErrorType() { return ErrorType.EMAIL_NOT_FOUND; }
}
