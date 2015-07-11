package com.ouchadam.auth;

import android.app.Activity;
import android.content.Context;
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

    public Observable<Token> acquireToken() {
        Token token = tokenStorage.getCurrentUserToken();

        if (token.isMissing()) {
            return foo.requestAnonymousAccessToken();
        }

        Log.e("!!!", "is missing? : " + token.isMissing() + " has expired : " + token.hasExpired() + " expiry : " + token.getExpiry() + " stamp : " + token.getTimeStamp());

        if (token.hasExpired()) {
            return foo.refreshToken(token);
        }
        return Observable.just(token);
    }

    public void createUserToken(Activity activity) {
        foo.requestUserAuthentication(activity);
    }

    public Observable<Token> requestNewToken(String oauthRedirect) {
        return foo.requestUserToken(oauthRedirect).map(saveToken());
    }

    private Func1<Token, Token> saveToken() {
        return new Func1<Token, Token>() {
            @Override
            public Token call(Token token) {
                Log.e("!!!", " saving token");
                String userName = new UserFetcher().fetchUserName(token);
                Log.e("!!!", userName);
                tokenStorage.storeToken(userName, token);
                return token;
            }
        };
    }

}
