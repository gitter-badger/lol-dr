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

    public void getToken(UserTokenRequest userTokenRequest, final Callback callback) {
        // TODO remove callback and become blocking
        if (storedTokenIsValid()) {
            callback.onTokenAcquired(getStoredToken(userTokenRequest));
        } else if (hasStoredToken(userTokenRequest)){
            refreshToken(userTokenRequest, callback);
        } else {
            requestNewToken(userTokenRequest, callback);
        }
    }

    private boolean storedTokenIsValid() {
        return false;
    }

    private boolean hasStoredToken(UserTokenRequest userTokenRequest) {
        return false;
    }

    private void refreshToken(UserTokenRequest userTokenRequest, Callback callback) {

    }

    private void requestNewToken(UserTokenRequest userTokenRequest, final Callback callback) {
        if (UserTokenRequest.Type.ANON == userTokenRequest.getType()) {
            foo.requestSignedOutToken(wrapCallback(callback));
        } else {
            foo.requestToken(userTokenRequest.getCode(), wrapCallback(callback));
        }
    }

    private Foo.Callback wrapCallback(final Callback callback) {
        return new Foo.Callback() {
            @Override
            public void onSuccess(Token token) {
                callback.onTokenAcquired(token);
            }
        };
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
