package id.ac.ui.cs.advprog.accountservice.util;
import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
public class CookieExtractor {
    private CookieExtractor() {
        // Utility class
    }

    public static Map<String, String> extract(String cookie) {
        var cookieRegex = new Cookie("pattern","([^=]+)="+"([^\\;]+);\\s?");
        cookieRegex.setHttpOnly(true);
        cookieRegex.setSecure(true);
        var pattern = Pattern.compile(cookieRegex.getValue());
        var matcher = pattern.matcher(cookie);
        Map<String, String> result = new HashMap<>();
        while (matcher.find()) {
            result.put(matcher.group(1), matcher.group(2));
        }
        return result;
    }
}