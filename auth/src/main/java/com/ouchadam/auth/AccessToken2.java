package com.ouchadam.auth;

class AccessToken2 implements Token {

    static final AccessToken2 MISSING = new AccessToken2("missing", "missing", "missing", -1L);

    private final String accoutName;
    private final String rawToken;
    private final String refreshToken;
    private final long expiryTime;

    public AccessToken2(String accoutName, String rawToken, String refreshToken, long expiryTime) {
        this.accoutName = accoutName;
        this.rawToken = rawToken;
        this.refreshToken = refreshToken;
        this.expiryTime = expiryTime;
    }

    public boolean isMissing() {
        return this == MISSING;
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
