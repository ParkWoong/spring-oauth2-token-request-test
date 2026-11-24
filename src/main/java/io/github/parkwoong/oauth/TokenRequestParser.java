package io.github.parkwoong.oauth;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TokenRequestParser {
    private static final ObjectMapper JSON =
            new ObjectMapper().enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);

    private TokenRequestParser() {}

    public static TokenRequest parse(HttpHeaders headers, MediaType type, String body) {
        if (headers == null
                || type == null
                || body == null
                || body.getBytes(StandardCharsets.UTF_8).length > 16384)
            throw new IllegalArgumentException("Invalid request");
        try {
            Map<String, String> fields;
            if (MediaType.APPLICATION_JSON.isCompatibleWith(type))
                fields = JSON.readValue(body, new TypeReference<Map<String, String>>() {});
            else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(type))
                fields = form(body);
            else throw new IllegalArgumentException("Unsupported content type");
            String[] basic = basic(headers.getFirst(HttpHeaders.AUTHORIZATION));
            String id = basic == null ? fields.get("client_id") : basic[0];
            String secret = basic == null ? fields.get("client_secret") : basic[1];
            if (basic != null
                    && ((fields.containsKey("client_id") && !id.equals(fields.get("client_id")))
                            || (fields.containsKey("client_secret")
                                    && !secret.equals(fields.get("client_secret")))))
                throw new IllegalArgumentException("Conflicting credentials");
            String grant = fields.get("grant_type");
            String code = fields.get("code");
            if (blank(id) || blank(secret) || !"authorization_code".equals(grant) || blank(code))
                throw new IllegalArgumentException("Invalid token request");
            return new TokenRequest(id, secret, grant, code);
        } catch (IllegalArgumentException error) {
            throw error;
        } catch (Exception error) {
            throw new IllegalArgumentException("Invalid token request", error);
        }
    }

    private static String[] basic(String authorization) {
        if (authorization == null) return null;
        if (!authorization.startsWith("Basic "))
            throw new IllegalArgumentException("Invalid authorization scheme");
        try {
            String decoded =
                    new String(
                            Base64.getDecoder().decode(authorization.substring(6)),
                            StandardCharsets.UTF_8);
            int colon = decoded.indexOf(':');
            if (colon <= 0 || colon == decoded.length() - 1)
                throw new IllegalArgumentException("Invalid Basic credentials");
            return new String[] {decoded.substring(0, colon), decoded.substring(colon + 1)};
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Invalid Basic credentials", error);
        }
    }

    private static Map<String, String> form(String body) throws Exception {
        Map<String, String> fields = new LinkedHashMap<>();
        for (String pair : body.split("&", -1)) {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], "UTF-8");
            String value = URLDecoder.decode(parts.length == 2 ? parts[1] : "", "UTF-8");
            if (fields.putIfAbsent(key, value) != null)
                throw new IllegalArgumentException("Duplicate form field");
        }
        return fields;
    }

    private static boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
