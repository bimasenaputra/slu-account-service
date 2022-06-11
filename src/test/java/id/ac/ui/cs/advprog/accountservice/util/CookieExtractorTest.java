package id.ac.ui.cs.advprog.accountservice.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CookieExtractorTest {
    @Test
    void success() {
        var idTokenStr = "idToken";
        var refreshTokenStr = "refreshToken";
        var cookieSample = "idToken=123; refreshToken=456; Secure; HttpOnly";
        Map<String, String> intended = new HashMap<>();
        intended.put(idTokenStr, "123");
        intended.put(refreshTokenStr, "456");
        var returned = CookieExtractor.extract(cookieSample);
        assertEquals(intended.get(idTokenStr), returned.get(idTokenStr));
        assertEquals(intended.get(refreshTokenStr), returned.get(refreshTokenStr));
        assertEquals(2, returned.size());
    }

}
