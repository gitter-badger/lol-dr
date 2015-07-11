package com.ouchadam.loldr;

import android.content.Context;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenAcquirer;
import com.ouchadam.loldr.data.TokenProvider;

public class FooToken implements TokenProvider {

    private final TokenAcquirer tokenAcquirer;

    public static FooToken newInstance(Context context) {
        return new FooToken(TokenAcquirer.newInstance(context));
    }

    public FooToken(TokenAcquirer tokenAcquirer) {
        this.tokenAcquirer = tokenAcquirer;
    }

    @Override
    public AccessToken provideAccessToken() {
        Token token = tokenAcquirer.acquireToken(null).toBlocking().first();
        return new TokenProvider.AccessToken(token.getAccessToken());
    }

}
