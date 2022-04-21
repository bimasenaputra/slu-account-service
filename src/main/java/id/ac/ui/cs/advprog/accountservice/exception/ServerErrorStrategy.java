package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class ServerErrorStrategy extends ErrorStrategy {
    @Override
    public String getMessage() {
        return "Error has occurred on server. Please try again in a few moments.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.INTERNAL_SERVER_ERROR; }
}
