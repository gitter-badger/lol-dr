package com.ouchadam.auth;

import android.accounts.AccountManager;
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



    private AccountManager accountManager;

    public static TokenAcquirer newInstance(Context context) {
        UUID deviceId = UUID.randomUUID();
        return new TokenAcquirer(new Foo(deviceId), TokenStorage.from(context));
    }

    public TokenAcquirer(Foo foo, TokenStorage tokenStorage) {
        this.foo = foo;
        this.tokenStorage = tokenStorage;
    }

    public Observable<Token> acquireToken() {
        AccessToken accessToken = tokenStorage.getCurrentUserToken();

        if (accessToken.isMissing() || (accessToken.isAnon() && accessToken.hasExpired())) {
            return foo.requestAnonymousAccessToken().map(saveAnonToken());
        }

        return accessToken.hasExpired() ? foo.refreshToken(accessToken) : Observable.<Token>just(accessToken);
    }

    public void createUserToken(Activity activity) {
        foo.requestUserAuthentication(activity);
    }

    public Observable<AccessToken> requestNewToken(String oauthRedirect) {
       return foo.requestUserToken(oauthRedirect).map(fetchUserName()).map(saveUserToken());
    }

    private Func1<AccessToken, Pair<AccessToken, String>> fetchUserName() {
        return new Func1<AccessToken, Pair<AccessToken, String>>() {
            @Override
            public Pair<AccessToken, String> call(AccessToken accessToken) {
                return Pair.create(accessToken, new UserFetcher().fetchUserName(accessToken));
            }
        };
    }

    private Func1<Pair<AccessToken, String>, AccessToken> saveUserToken() {
        return new Func1<Pair<AccessToken, String>, AccessToken>() {
            @Override
            public AccessToken call(Pair<AccessToken, String> input) {
                Log.e("!!!", " saving token");
                tokenStorage.storeToken(input.second, input.first);
                return input.first;
            }
        };
    }

    private Func1<AccessToken, Token> saveAnonToken() {
        return new Func1<AccessToken, Token>() {
            @Override
            public AccessToken call(AccessToken input) {
                Log.e("!!!", " saving token");
                tokenStorage.storeToken("anon", input);
                return input;
            }
        };
    }

}
