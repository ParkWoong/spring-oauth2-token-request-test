package io.github.parkwoong.oauth;

import java.net.URLEncoder;

public final class AuthorizationCodeForm {
    private AuthorizationCodeForm() {}

    public static String newBodyFromCode(
            String code, String clientId, String clientSecret, String redirectUri) {
        if (blank(code) || blank(clientId) || blank(clientSecret) || blank(redirectUri))
            throw new IllegalArgumentException("Invalid request");
        try {
            return "client_id="
                    + encode(clientId)
                    + "&client_secret="
                    + encode(clientSecret)
                    + "&grant_type=authorization_code"
                    + "&code="
                    + encode(code)
                    + "&redirect_uri="
                    + encode(redirectUri);
        } catch (Exception error) {
            throw new IllegalStateException("Form encoding failed", error);
        }
    }

    private static String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

    private static boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
