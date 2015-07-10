package com.ouchadam.auth;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

import rx.Observable;
import rx.functions.Func1;

public class TokenAcquirer {

    private final Foo foo;
    private final TokenStorage tokenStorage;

    public static TokenAcquirer newInstance(Context context) {
        UUID deviceId = UUID.randomUUID();
        return new TokenAcquirer(new Foo(deviceId), TokenStorage.from(context));
    }

    public TokenAcquirer(Foo foo, TokenStorage tokenStorage) {
        this.foo = foo;
        this.tokenStorage = tokenStorage;
    }

    public Observable<Token> acquireToken(UserTokenRequest userTokenRequest) {
        Token token = tokenStorage.getToken(userTokenRequest);

        Log.e("!!!", "missing : " + token.isMissing() + " : expired : " + token.hasExpired());

        if (token.isMissing() || (userTokenRequest.getType() == UserTokenRequest.Type.ANON && token.hasExpired())) {
            return requestNewToken(userTokenRequest).map(saveToken());
        } else if (userTokenRequest.getType() != UserTokenRequest.Type.ANON && token.hasExpired()) {
            return refreshToken(token).map(saveToken());
        } else {
            return Observable.just(token);
        }
    }

    private Func1<Token, Token> saveToken() {
        return new Func1<Token, Token>() {
            @Override
            public Token call(Token token) {
                tokenStorage.storeToken(token);
                return token;
            }
        };
    }

    private Observable<Token> refreshToken(Token token) {
        return foo.refreshToken(token);
    }

    private Observable<Token> requestNewToken(UserTokenRequest userTokenRequest) {
        if (UserTokenRequest.Type.ANON == userTokenRequest.getType()) {
            return foo.requestAnonymousAccessToken();
        } else {
            return foo.requestUserToken(userTokenRequest.getCode());
        }
    }

    public void createUserToken(Activity activity) {
        foo.requestUserAuthentication(activity);
    }

    private static class TokenStorage {

        public static final String RAW = "raw";
        public static final String EXPIRY = "expiry";
        public static final String TIMESTAMP = "timestamp";
        private final SharedPreferences preferences;

        public static TokenStorage from(Context context) {
            return new TokenStorage(context.getSharedPreferences("com.ouchadam.loldr.token", Context.MODE_PRIVATE));
        }

        private TokenStorage(SharedPreferences preferences) {
            this.preferences = preferences;
        }

        public Token getToken(UserTokenRequest userTokenRequest) {
            if (preferences.contains("raw")) {
                return new Token(preferences.getString(RAW, ""), preferences.getInt(EXPIRY, 0), preferences.getLong(TIMESTAMP, 0));
            } else {
                return Token.MISSING;
            }
        }

        public void storeToken(Token token) {
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(RAW, token.getRawToken());
            editor.putInt(EXPIRY, token.getExpiry());
            editor.putLong(TIMESTAMP, token.getTimeStamp());

            editor.apply();

        }

    }

}
