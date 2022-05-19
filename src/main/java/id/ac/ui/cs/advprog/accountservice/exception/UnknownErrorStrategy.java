package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class UnknownErrorStrategy extends ErrorStrategy {
    @Override
    public String getMessage() {
        return "An unknown error has occurred. Please try again in a few moments.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.INTERNAL_SERVER_ERROR; }

    @Override
    public ErrorType getErrorType() { return ErrorType.UNKNOWN_ERROR; }
}
