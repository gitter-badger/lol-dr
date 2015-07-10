package com.ouchadam.auth;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.UUID;

import rx.Observable;

public class TokenAcquirer {

    private final Foo foo;
    private final TokenStorage tokenStorage;

    public static TokenAcquirer newInstance() {
        UUID deviceId = UUID.randomUUID();
        return new TokenAcquirer(new Foo(deviceId), null);
    }

    public TokenAcquirer(Foo foo, TokenStorage tokenStorage) {
        this.foo = foo;
        this.tokenStorage = tokenStorage;
    }

    public Observable<Token> acquireToken(UserTokenRequest userTokenRequest) {
        if (storedTokenIsValid()) {
            return getStoredToken(userTokenRequest);
        } else if (hasStoredToken(userTokenRequest)){
            return refreshToken(userTokenRequest);
        } else {
            return requestNewToken(userTokenRequest);
        }
    }

    private boolean storedTokenIsValid() {

        Token token = tokenStorage.getToken();

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

    private static class TokenStorage {

        private final SharedPreferences preferences;

        private TokenStorage(SharedPreferences preferences) {
            this.preferences = preferences;
        }

        public Token getToken() {
            return null;
        }
    }

}
