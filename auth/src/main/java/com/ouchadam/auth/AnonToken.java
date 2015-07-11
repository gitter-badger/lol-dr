package com.ouchadam.auth;

public class AnonToken implements Token {

    private final String accessToken;
    private final long expiryTime;

    public AnonToken(String accessToken, long expiryTime) {
        this.accessToken = accessToken;
        this.expiryTime = expiryTime;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() >= expiryTime;
    }

    public long getExpiryTime() {
        return expiryTime;
    }
}
