package com.ouchadam.auth;

import android.app.Activity;

import java.util.UUID;

import rx.Observable;

public class TokenProvider {

    private final Foo foo;

    public static TokenProvider newInstance() {
        UUID deviceId = UUID.randomUUID();
        return new TokenProvider(new Foo(deviceId));
    }

    public TokenProvider(Foo foo) {
        this.foo = foo;
    }

    public Observable<Token> getToken(UserTokenRequest userTokenRequest) {
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

    private Observable<Token> refreshToken(UserTokenRequest userTokenRequest) {
        return null;
    }

    private Observable<Token> requestNewToken(UserTokenRequest userTokenRequest) {
        if (UserTokenRequest.Type.ANON == userTokenRequest.getType()) {
            return foo.requestAnonymousAccessToken();
        } else {
            return foo.requestUserToken(userTokenRequest.getCode());
        }
    }

    private Observable<Token> getStoredToken(UserTokenRequest userTokenRequest) {
        return null;
    }

    public void createUserToken(Activity activity) {
        foo.requestUserAuthentication(activity);
    }

    public interface Callback {
        void onTokenAcquired(Token token);
    }

}
