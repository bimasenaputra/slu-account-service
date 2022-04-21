package id.ac.ui.cs.advprog.accountservice.util;

import id.ac.ui.cs.advprog.accountservice.exception.ErrorConditionFactory;
import id.ac.ui.cs.advprog.accountservice.exception.ErrorType;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorHandler {

    public static ResponseEntity<Map<String, Object>> resolveError(ErrorType errorType) {
        final ErrorConditionFactory errorConditionFactory = new ErrorConditionFactory();
        Map<String, Object> response = new LinkedHashMap<>();
        var strategy = errorConditionFactory.getErrorStrategy(errorType);
        response.put("message",strategy.getMessage());
        return ResponseEntity.status(strategy.getStatus()).body(response);
    }
}
