package io.github.parkwoong.oauth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class TokenRequestController {
    @PostMapping("/api/oauth/basic")
    public ResponseEntity<Map<String, Object>> basic(@RequestHeader HttpHeaders headers) {
        String[] clientInfo = HttpUtils.getClientInfo(headers);
        if (clientInfo == null) return ResponseEntity.badRequest().build();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("clientId", clientInfo[0]);
        result.put("secretPresent", true);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/oauth/form")
    public ResponseEntity<Map<String, String>> form(@RequestBody Map<String, String> input) {
        try {
            String encoded =
                    AuthorizationCodeForm.newBodyFromCode(
                            input.get("code"),
                            input.get("clientId"),
                            input.get("clientSecret"),
                            input.get("redirectUri"));
            return ResponseEntity.ok(
                    Collections.singletonMap(
                            "form",
                            encoded.replaceFirst("client_secret=[^&]*", "client_secret=REDACTED")));
        } catch (IllegalArgumentException error) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", "Missing token request field"));
        }
    }
}
