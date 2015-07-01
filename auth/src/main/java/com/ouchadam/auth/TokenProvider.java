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

    public void getToken(User user, final Callback callback) {
        if (storedTokenIsValid()) {
            callback.onTokenAcquired(getStoredToken(user));
        } else if (hasStoredToken(user)){
            refreshToken(user, callback);
        } else {
            requestNewToken(user, callback);
        }
    }

    private boolean storedTokenIsValid() {
        return false;
    }

    private boolean hasStoredToken(User user) {
        return false;
    }

    private void refreshToken(User user, Callback callback) {

    }

    private void requestNewToken(User user, final Callback callback) {
        if (User.Type.ANON == user.getType()) {
            foo.requestSignedOutToken(wrapCallback(callback));
        } else {
            foo.requestToken(user.getCode(), wrapCallback(callback));
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

    private Token getStoredToken(User user) {
        return null;
    }

    public void createUserToken(Activity activity) {
        foo.requestUserAuthentication(activity);
    }

    public interface Callback {
        void onTokenAcquired(Token token);
    }

}
