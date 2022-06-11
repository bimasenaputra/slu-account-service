package id.ac.ui.cs.advprog.accountservice.dto;

import id.ac.ui.cs.advprog.accountservice.exception.ErrorConditionFactory;
import lombok.Getter;

import java.util.List;

@Getter
public class FirebaseErrorResponse {
    private List<FirebaseErrorDetail> errors;
    private String code;
    private String message;

    public String parseMessage() {
        var errorFactory = new ErrorConditionFactory();
        var strategy = errorFactory.getErrorStrategy(message);
        return strategy.getMessage();
    }
}
