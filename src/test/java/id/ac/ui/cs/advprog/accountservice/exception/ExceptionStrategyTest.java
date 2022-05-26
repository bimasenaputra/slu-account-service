package id.ac.ui.cs.advprog.accountservice.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Null;

public class ExceptionStrategyTest {
    @Mock
    private ErrorConditionFactory factory = new ErrorConditionFactory();

    @Test
    public void EmailExistsTest() {
        var mock1 = factory.getErrorStrategy("EMAIL_EXISTS");
        var mock2 = factory.getErrorStrategy(ErrorType.EMAIL_EXISTS);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("The email address is already in use by another account.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, mock1.getStatus());
        Assertions.assertEquals(ErrorType.EMAIL_EXISTS, mock1.getErrorType());
    }

    @Test
    public void EmailNotFoundTest() {
        var mock1 = factory.getErrorStrategy("EMAIL_NOT_FOUND");
        var mock2 = factory.getErrorStrategy(ErrorType.EMAIL_NOT_FOUND);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("There is no user record corresponding to this email.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, mock1.getStatus());
        Assertions.assertEquals(ErrorType.EMAIL_NOT_FOUND, mock1.getErrorType());
    }

    @Test
    public void InvalidEmailTest() {
        var mock1 = factory.getErrorStrategy("INVALID_EMAIL");
        var mock2 = factory.getErrorStrategy(ErrorType.INVALID_EMAIL);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("There is no user record corresponding to this email.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, mock1.getStatus());
        Assertions.assertEquals(ErrorType.INVALID_EMAIL, mock1.getErrorType());
    }

    @Test
    public void InvalidPasswordTest() {
        var mock1 = factory.getErrorStrategy("INVALID_PASSWORD");
        var mock2 = factory.getErrorStrategy(ErrorType.INVALID_PASSWORD);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("The password is invalid.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, mock1.getStatus());
        Assertions.assertEquals(ErrorType.INVALID_PASSWORD, mock1.getErrorType());
    }

    @Test
    public void InvalidRefreshTokenTest() {
        var mock1 = factory.getErrorStrategy("INVALID_REFRESH_TOKEN");
        var mock2 = factory.getErrorStrategy(ErrorType.INVALID_REFRESH_TOKEN);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("An invalid refresh token is provided.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.FORBIDDEN, mock1.getStatus());
        Assertions.assertEquals(ErrorType.INVALID_REFRESH_TOKEN, mock1.getErrorType());
    }

    @Test
    public void ServerErrorTest() {
        var mock1 = factory.getErrorStrategy("SERVER_ERROR");
        var mock2 = factory.getErrorStrategy(ErrorType.SERVER_ERROR);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("Error has occurred on server. Please try again in a few moments.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mock1.getStatus());
        Assertions.assertEquals(ErrorType.SERVER_ERROR, mock1.getErrorType());
    }

    @Test
    public void TimeoutTest() {
        var mock1 = factory.getErrorStrategy("TIMEOUT");
        var mock2 = factory.getErrorStrategy(ErrorType.TIMEOUT);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("Request took longer and timed out. Check your internet, then try again.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.GATEWAY_TIMEOUT, mock1.getStatus());
        Assertions.assertEquals(ErrorType.TIMEOUT, mock1.getErrorType());
    }

    @Test
    public void UnknownErrorTest() {
        var mock1 = factory.getErrorStrategy("UNKNOWN_ERROR");
        var mock2 = factory.getErrorStrategy(ErrorType.UNKNOWN_ERROR);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("An unknown error has occurred. Please try again in a few moments.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mock1.getStatus());
        Assertions.assertEquals(ErrorType.UNKNOWN_ERROR, mock1.getErrorType());
    }

    @Test
    public void UsernameExistsTest() {
        var mock1 = factory.getErrorStrategy("USERNAME_EXISTS");
        var mock2 = factory.getErrorStrategy(ErrorType.USERNAME_EXISTS);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("The username is already in use by another account.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, mock1.getStatus());
        Assertions.assertEquals(ErrorType.USERNAME_EXISTS, mock1.getErrorType());
    }

    @Test
    public void UsernameNotFoundTest() {
        var mock1 = factory.getErrorStrategy("USERNAME_NOT_FOUND");
        var mock2 = factory.getErrorStrategy(ErrorType.USERNAME_NOT_FOUND);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("There is no user record corresponding to this username.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, mock1.getStatus());
        Assertions.assertEquals(ErrorType.USERNAME_NOT_FOUND, mock1.getErrorType());
    }

    @Test
    public void WeakPasswordTest() {
        var mock1 = factory.getErrorStrategy("WEAK_PASSWORD");
        var mock2 = factory.getErrorStrategy(ErrorType.WEAK_PASSWORD);
        Assertions.assertEquals(mock1.getMessage(), mock2.getMessage());
        Assertions.assertEquals(mock1.getErrorType(), mock2.getErrorType());
        Assertions.assertEquals(mock1.getStatus(), mock2.getStatus());
        Assertions.assertEquals("The password must be 6 characters long or more.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, mock1.getStatus());
        Assertions.assertEquals(ErrorType.WEAK_PASSWORD, mock1.getErrorType());
    }

    @Test
    public void NoNameTest() {
        var mock1 = factory.getErrorStrategy((String) null);
        Assertions.assertEquals("An unknown error has occurred. Please try again in a few moments.", mock1.getMessage());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mock1.getStatus());
        Assertions.assertEquals(ErrorType.UNKNOWN_ERROR, mock1.getErrorType());
        Assertions.assertThrows(NullPointerException.class, () -> factory.getErrorStrategy((ErrorType) null));
    }
}
