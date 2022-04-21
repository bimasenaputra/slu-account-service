package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class TimeoutStrategy extends ErrorStrategy {
    @Override
    public String getMessage() {
        return "Request took longer and timed out. Check your internet, then try again.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.GATEWAY_TIMEOUT; }
}
