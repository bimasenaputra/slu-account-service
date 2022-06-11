package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class ServerErrorStrategy implements ErrorStrategy {
    @Override
    public String getMessage() {
        return "Error has occurred on server. Please try again in a few moments.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.INTERNAL_SERVER_ERROR; }

    @Override
    public ErrorType getErrorType() { return ErrorType.SERVER_ERROR; }
}
