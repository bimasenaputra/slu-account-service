package id.ac.ui.cs.advprog.accountservice.exception;

import com.google.gson.Gson;
import id.ac.ui.cs.advprog.accountservice.dto.FirebaseError;
import id.ac.ui.cs.advprog.accountservice.dto.FirebaseErrorResponse;
import io.netty.handler.timeout.TimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private final ErrorConditionFactory errorConditionFactory = new ErrorConditionFactory();

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Map<String, String>> handleAccountNotFound(RuntimeException ex) {
        var error = errorConditionFactory.getErrorStrategy(ex.getMessage());
        return resolveError(error.getErrorType());
    }

    @ExceptionHandler(value = {WebClientResponseException.class})
    protected ResponseEntity<Map<String, String>> handleWebClientError(WebClientResponseException  ex) {
        FirebaseError error = new Gson().fromJson(ex.getResponseBodyAsString(), FirebaseError.class);
        FirebaseErrorResponse responseBody = error.getError();
        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", responseBody.parseMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = {RestClientException.class})
    protected ResponseEntity<Map<String, String>> handleRestClientError() {
        return resolveError(ErrorType.INVALID_REFRESH_TOKEN);
    }

    @ExceptionHandler(value = {TimeoutException.class})
    protected ResponseEntity<Map<String, String>> handleTimeout() {
        return resolveError(ErrorType.TIMEOUT);
    }

    @ExceptionHandler(value = {AssertionError.class})
    protected ResponseEntity<Map<String, String>> handleNullAssertion() {
        return resolveError(ErrorType.SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Map<String, String>> handleException() {
        return resolveError(ErrorType.UNKNOWN_ERROR);
    }

    private ResponseEntity<Map<String, String>> resolveError(ErrorType errorType) {
        Map<String, String> response = new LinkedHashMap<>();
        var strategy = errorConditionFactory.getErrorStrategy(errorType);
        response.put("message",strategy.getMessage());
        return ResponseEntity.status(strategy.getStatus()).body(response);
    }
}
