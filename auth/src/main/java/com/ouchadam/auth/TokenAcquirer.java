package com.ouchadam.auth;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.Pair;
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

        if (token.isMissing() || (token.isAnon() && token.hasExpired())) {
            return foo.requestAnonymousAccessToken().map(saveAnonToken());
        }

        return token.hasExpired() ? foo.refreshToken(token) : Observable.just(token);
    }

    public void createUserToken(Activity activity) {
        foo.requestUserAuthentication(activity);
    }

    public Observable<Token> requestNewToken(String oauthRedirect) {
       return foo.requestUserToken(oauthRedirect).map(fetchUserName()).map(saveUserToken());
    }


    private Func1<Token, Pair<Token, String>> fetchUserName() {
        return new Func1<Token, Pair<Token, String>>() {
            @Override
            public Pair<Token, String> call(Token token) {
                return Pair.create(token, new UserFetcher().fetchUserName(token));
            }
        };
    }

    private Func1<Pair<Token, String>, Token> saveUserToken() {
        return new Func1<Pair<Token, String>, Token>() {
            @Override
            public Token call(Pair<Token, String> input) {
                Log.e("!!!", " saving token");
                tokenStorage.storeToken(input.second, input.first);
                return input.first;
            }
        };
    }

    private Func1<Token, Token> saveAnonToken() {
        return new Func1<Token, Token>() {
            @Override
            public Token call(Token input) {
                Log.e("!!!", " saving token");
                tokenStorage.storeToken("anon", input);
                return input;
            }
        };
    }

}
