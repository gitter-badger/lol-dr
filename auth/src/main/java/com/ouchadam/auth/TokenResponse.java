package com.ouchadam.auth;

class TokenResponse {

    private final String rawToken;
    private final String refreshToken;
    private final int expiryInSeconds;
    private final long acquiredAtTimeStamp;

    public TokenResponse(String rawToken, String refreshToken, int expiryInSeconds, long acquiredAtTimeStamp) {
        this.rawToken = rawToken;
        this.refreshToken = refreshToken;
        this.expiryInSeconds = expiryInSeconds;
        this.acquiredAtTimeStamp = acquiredAtTimeStamp;
    }

    public String getRawToken() {
        return rawToken;
    }

    public int getExpiry() {
        return expiryInSeconds;
    }

    public long getTimeStamp() {
        return acquiredAtTimeStamp;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
