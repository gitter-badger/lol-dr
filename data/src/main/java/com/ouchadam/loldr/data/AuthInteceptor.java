package com.ouchadam.loldr.data;

import android.util.Log;

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

        Log.e("!!!", "token : " + accessToken.get());

        return chain.proceed(chain.request().newBuilder()
                        .addHeader("Authorization", "bearer " + accessToken.get())
                        .build()
        );
    }
}
