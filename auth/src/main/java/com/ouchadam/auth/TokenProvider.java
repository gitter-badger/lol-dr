package com.ouchadam.auth;

import android.app.Activity;

import java.util.UUID;

public class TokenProvider {

    private final Foo foo;

    public static TokenProvider newInstance() {
        UUID deviceId = UUID.randomUUID();
        return new TokenProvider(new Foo(deviceId));
    }

    public TokenProvider(Foo foo) {
        this.foo = foo;
    }

    public Token getToken(UserTokenRequest userTokenRequest) {
        // TODO remove callback and become blocking
        if (storedTokenIsValid()) {
            return getStoredToken(userTokenRequest);
        } else if (hasStoredToken(userTokenRequest)){
            return refreshToken(userTokenRequest);
        } else {
            return requestNewToken(userTokenRequest);
        }
    }

    private boolean storedTokenIsValid() {
        return false;
    }

    private boolean hasStoredToken(UserTokenRequest userTokenRequest) {
        return false;
    }

    private Token refreshToken(UserTokenRequest userTokenRequest) {
        return null;
    }

    private Token requestNewToken(UserTokenRequest userTokenRequest) {
        if (UserTokenRequest.Type.ANON == userTokenRequest.getType()) {
            return foo.requestAnonymousAccessToken().toBlocking().first();
        } else {
            return foo.requestToken(userTokenRequest.getCode());
        }
    }

    private Token getStoredToken(UserTokenRequest userTokenRequest) {
        return null;
    }

    public void createUserToken(Activity activity) {
        foo.requestUserAuthentication(activity);
    }

    public interface Callback {
        void onTokenAcquired(Token token);
    }

}
