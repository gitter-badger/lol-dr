package com.ouchadam.loldr.data;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

class AuthInteceptor implements Interceptor {

    private final TokenProvider tokenProvider;

    AuthInteceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        TokenProvider.AccessToken accessToken = tokenProvider.provideAccessToken();

        return chain.proceed(chain.request().newBuilder()
                        .addHeader("Authorization", "bearer " + accessToken.get())
                        .build()
        );
    }
}
