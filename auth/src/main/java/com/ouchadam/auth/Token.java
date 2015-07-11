package com.ouchadam.auth;

import java.util.concurrent.TimeUnit;

public class Token {

    static final Token MISSING = new Token("missing", "missing", -1, -1L);
    private static final int EXPIRY_BUFFER = 10000;

    private final String rawToken;
    private final String refreshToken;
    private final int expiryInSeconds;
    private final long acquiredAtTimeStamp;

    public Token(String rawToken, String refreshToken, int expiryInSeconds, long acquiredAtTimeStamp) {
        this.rawToken = rawToken;
        this.refreshToken = refreshToken;
        this.expiryInSeconds = expiryInSeconds;
        this.acquiredAtTimeStamp = acquiredAtTimeStamp;
    }

    public String getRawToken() {
        return rawToken;
    }

    public boolean hasExpired() {
        return (acquiredAtTimeStamp + TimeUnit.SECONDS.toMillis(expiryInSeconds)) <= (System.currentTimeMillis() - EXPIRY_BUFFER);
    }

    public boolean isMissing() {
        return this == MISSING;
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
