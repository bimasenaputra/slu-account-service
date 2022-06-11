package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public interface ErrorStrategy {
    String getMessage();
    HttpStatus getStatus();
    ErrorType getErrorType();
}
