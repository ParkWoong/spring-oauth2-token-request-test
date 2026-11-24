package io.github.parkwoong.oauth;

public final class TokenRequest {
    private final String clientId;
    private final String clientSecret;
    private final String grantType;
    private final String code;

    public TokenRequest(String clientId, String clientSecret, String grantType, String code) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
        this.code = code;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getCode() {
        return code;
    }
}
