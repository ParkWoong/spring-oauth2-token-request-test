package io.github.parkwoong.oauth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class TokenRequestTest {
    @Test
    void basicAndJsonBecomeEncodedAuthorizationCodeForm() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(
                Base64.getEncoder()
                        .encodeToString("sample:sec:ret".getBytes(StandardCharsets.UTF_8)));
        TokenRequest request =
                TokenRequestParser.parse(
                        headers,
                        MediaType.APPLICATION_JSON,
                        "{\"grant_type\":\"authorization_code\",\"code\":\"a&b+\"}");
        assertEquals("sample", request.getClientId());
        assertEquals("sec:ret", request.getClientSecret());
        assertEquals(
                "client_id=sample&client_secret=sec%3Aret&grant_type=authorization_code&code=a%26b%2B&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback",
                AuthorizationCodeForm.build(request, "http://localhost/callback"));
    }

    @Test
    void formAndInvalidInputs() {
        TokenRequest request =
                TokenRequestParser.parse(
                        new HttpHeaders(),
                        MediaType.APPLICATION_FORM_URLENCODED,
                        "client_id=sample&client_secret=secret&grant_type=authorization_code&code=abc");
        assertEquals("abc", request.getCode());
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        TokenRequestParser.parse(
                                new HttpHeaders(),
                                MediaType.APPLICATION_FORM_URLENCODED,
                                "client_id=x&client_id=y&client_secret=s&grant_type=authorization_code&code=c"));
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        TokenRequestParser.parse(
                                new HttpHeaders(), MediaType.APPLICATION_JSON, "{broken"));
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        TokenRequestParser.parse(
                                new HttpHeaders(),
                                MediaType.APPLICATION_FORM_URLENCODED,
                                "client_id=x&client_secret=s&grant_type=refresh_token&code=c"));
        assertThrows(
                IllegalArgumentException.class,
                () -> AuthorizationCodeForm.build(request, "http://example.com/callback"));
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        TokenRequestParser.parse(
                                new HttpHeaders(),
                                MediaType.APPLICATION_JSON,
                                "{\"client_id\":\"a\",\"client_id\":\"b\",\"client_secret\":\"s\",\"grant_type\":\"authorization_code\",\"code\":\"c\"}"));
        HttpHeaders conflict = new HttpHeaders();
        conflict.setBasicAuth(
                Base64.getEncoder().encodeToString("a:s".getBytes(StandardCharsets.UTF_8)));
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        TokenRequestParser.parse(
                                conflict,
                                MediaType.APPLICATION_FORM_URLENCODED,
                                "client_id=b&grant_type=authorization_code&code=c"));
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        TokenRequestParser.parse(
                                new HttpHeaders(),
                                MediaType.APPLICATION_JSON,
                                "{\"code\":\""
                                        + new String(new char[6000]).replace('\0', '가')
                                        + "\"}"));
    }
}
