package com.ouchadam.auth;

class UserToken implements Token {

    private final String accoutName;
    private final String rawToken;
    private final String refreshToken;
    private final long expiryTime;

    public UserToken(String accountName, String rawToken, String refreshToken, long expiryTime) {
        this.accoutName = accountName;
        this.rawToken = rawToken;
        this.refreshToken = refreshToken;
        this.expiryTime = expiryTime;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccoutName() {
        return accoutName;
    }

    @Override
    public String getAccessToken() {
        return rawToken;
    }
}
