package id.ac.ui.cs.advprog.accountservice.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CookieExtractorTest {
    @Test
    public void success() {
        var cookieSample = "idToken=123; refreshToken=456; Secure; HttpOnly";
        Map<String, String> intended = new HashMap<>();
        intended.put("idToken", "123");
        intended.put("refreshToken", "456");
        var returned = CookieExtractor.extract(cookieSample);
        assertEquals(intended.get("idToken"), returned.get("idToken"));
        assertEquals(intended.get("refreshToken"), returned.get("refreshToken"));
        assertEquals(2, returned.size());
    }
}
