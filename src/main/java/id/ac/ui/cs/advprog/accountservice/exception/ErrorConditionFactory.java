package id.ac.ui.cs.advprog.accountservice.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ErrorConditionFactory {
    private final Map<String, ErrorStrategy> conditions = new HashMap<>();

    public ErrorConditionFactory() {
        conditions.put("INVALID_REFRESH_TOKEN", new InvalidRefreshTokenStrategy());
        conditions.put("EMAIL_EXISTS", new EmailExistsStrategy());
        conditions.put("EMAIL_NOT_FOUND", new EmailNotFoundStrategy());
        conditions.put("INVALID_EMAIL", new InvalidEmailStrategy());
        conditions.put("INVALID_PASSWORD", new InvalidPasswordStrategy());
        conditions.put("WEAK_PASSWORD", new WeakPasswordStrategy());
        conditions.put("USERNAME_EXISTS", new UsernameExistsStrategy());
        conditions.put("USERNAME_NOT_FOUND", new UsernameNotFoundStrategy());
        conditions.put("TIMEOUT", new TimeoutStrategy());
        conditions.put("SERVER_ERROR", new ServerErrorStrategy());
        conditions.put("UNKNOWN_ERROR", new UnknownErrorStrategy());
    }

    public ErrorStrategy getErrorStrategy(String condition) {
        var strategy = conditions.get(condition);
        return strategy == null ? new UnknownErrorStrategy() : strategy;
    }

    public ErrorStrategy getErrorStrategy(ErrorType condition) {
        var strategy = conditions.get(condition.toString());
        return strategy == null ? new UnknownErrorStrategy() : strategy;
    }
}
