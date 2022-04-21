package id.ac.ui.cs.advprog.accountservice.exception;

import org.springframework.http.HttpStatus;

public abstract class ErrorStrategy {
    public abstract String getMessage();
    public abstract HttpStatus getStatus();
}
