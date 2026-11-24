package io.github.parkwoong.oauth;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class TokenRequestTest {
    @Test
    void readsBasicClientInfo() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(
                Base64.getEncoder()
                        .encodeToString("sample:sec:ret".getBytes(StandardCharsets.UTF_8)));
        assertArrayEquals(new String[] {"sample", "sec:ret"}, HttpUtils.getClientInfo(headers));
        assertNull(HttpUtils.getClientInfo(new HttpHeaders()));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic invalid-base64");
        assertNull(HttpUtils.getClientInfo(headers));
    }

    @Test
    void buildsEncodedAuthorizationCodeForm() {
        assertEquals(
                "client_id=sample&client_secret=sec%3Aret&grant_type=authorization_code&code=a%26b%2B&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback",
                AuthorizationCodeForm.newBodyFromCode(
                        "a&b+", "sample", "sec:ret", "http://localhost/callback"));
        assertThrows(
                IllegalArgumentException.class,
                () -> AuthorizationCodeForm.newBodyFromCode("", "sample", "secret", "uri"));
    }
}
