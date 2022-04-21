package id.ac.ui.cs.advprog.accountservice.exception;

public enum ErrorType {
    USER_DISABLED ("USER_DISABLED"),
    INVALID_ID_TOKEN ("INVALID_ID_TOKEN"),
    INVALID_REFRESH_TOKEN ("INVALID_REFRESH_TOKEN"),
    EMAIL_EXISTS ("EMAIL_EXISTS"),
    EMAIL_NOT_FOUND ("EMAIL_NOT_FOUND"),
    INVALID_EMAIL ("INVALID_EMAIL"),
    INVALID_PASSWORD ("INVALID_PASSWORD"),
    WEAK_PASSWORD ("WEAK_PASSWORD"),
    USERNAME_EXISTS ("USERNAME_EXISTS"),
    WRONG_REQUEST_PAYLOAD ("WRONG_REQUEST_PAYLOAD"),
    TIMEOUT ("TIMEOUT"),
    SERVER_ERROR ("SERVER_ERROR"),
    UNKNOWN_ERROR ("UNKNOWN_ERROR");

    private String name;

    private ErrorType(String name) { this.name = name; }

    public String toString() { return name; }
}
