package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class WrongRequestPayloadStrategy extends ErrorStrategy {
    @Override
    public String getMessage() {
        return "Wrong request body payload.";
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.BAD_REQUEST; }
}
