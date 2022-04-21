package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenStrategy extends ErrorStrategy {
    @Override
    public String getMessage() { return "An invalid refresh token is provided."; }

    @Override
    public HttpStatus getStatus() { return HttpStatus.FORBIDDEN; }
}
