package io.github.parkwoong.oauth;

import java.net.URI;
import java.net.URLEncoder;

public final class AuthorizationCodeForm {
    private AuthorizationCodeForm() {}

    public static String build(TokenRequest request, String redirectUri) {
        if (request == null || redirectUri == null)
            throw new IllegalArgumentException("Invalid request");
        URI uri = URI.create(redirectUri);
        if (!uri.isAbsolute()
                || uri.getHost() == null
                || uri.getFragment() != null
                || uri.getUserInfo() != null
                || !("https".equals(uri.getScheme())
                        || ("http".equals(uri.getScheme()) && "localhost".equals(uri.getHost()))))
            throw new IllegalArgumentException("Invalid redirect URI");
        try {
            return "client_id="
                    + encode(request.getClientId())
                    + "&client_secret="
                    + encode(request.getClientSecret())
                    + "&grant_type="
                    + encode(request.getGrantType())
                    + "&code="
                    + encode(request.getCode())
                    + "&redirect_uri="
                    + encode(redirectUri);
        } catch (Exception error) {
            throw new IllegalStateException("Form encoding failed", error);
        }
    }

    private static String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }
}
