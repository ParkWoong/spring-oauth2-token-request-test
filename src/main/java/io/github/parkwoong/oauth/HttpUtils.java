package io.github.parkwoong.oauth;

import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class HttpUtils {
    private HttpUtils() {}

    public static String[] getClientInfo(HttpHeaders headers) {
        if (headers == null) return null;

        for (String value : headers.getOrEmpty(HttpHeaders.AUTHORIZATION)) {
            if (!value.startsWith("Basic ")) continue;
            try {
                String decoded =
                        new String(
                                Base64.getDecoder().decode(value.substring(6)),
                                StandardCharsets.UTF_8);
                int separator = decoded.indexOf(':');
                if (separator > 0 && separator < decoded.length() - 1)
                    return new String[] {
                        decoded.substring(0, separator), decoded.substring(separator + 1)
                    };
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }
        return null;
    }
}
