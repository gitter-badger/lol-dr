package com.ouchadam.auth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

class Foo {

    public static final String BASE_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=";
    public static final String REDIRECT_URI = "http://com.ouchadam.kanto";
    public static final String CLIENT_ID = "6224o4_ylYWflQ";

    private final UUID uniqueDeviceId;

    public Foo(UUID uniqueDeviceId) {
        this.uniqueDeviceId = uniqueDeviceId;
    }

    public void requestUserAuthentication(Activity activity) {
        String responseType = "code";
        String requestId = "RANDOM_STRING";
        String duration = "permanent";
        String scope = "read,identity";

        Intent intent = new Intent(activity, OAuthWebViewActivity.class);
        intent.setData(
                Uri.parse(
                        BASE_URL
                                + CLIENT_ID
                                + "&response_type=" + responseType
                                + "&state=" + requestId
                                + "&redirect_uri=" + REDIRECT_URI
                                + "&duration=" + duration
                                + "&scope=" + scope));

        activity.startActivityForResult(intent, 100);
    }

    public Observable<Token> requestAnonymousAccessToken() {
        return Observable.create(
                new Observable.OnSubscribe<Token>() {
                    @Override
                    public void call(Subscriber<? super Token> subscriber) {
                        Token anonymousAccessToken;
                        try {
                            anonymousAccessToken = getAnonymousAccessToken();
                        } catch (IOException e) {
                            subscriber.onError(e);
                            return;
                        }

                        subscriber.onNext(anonymousAccessToken);
                        subscriber.onCompleted();
                    }
                }
        );
    }

    private Token getAnonymousAccessToken() throws IOException {
        MediaType textMediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .url("https://www.reddit.com/api/v1/access_token")
                .post(RequestBody.create(textMediaType, "grant_type=https://oauth.reddit.com/grants/installed_client&device_id=" + uniqueDeviceId.toString()))
                .addHeader("Authorization", Credentials.basic(CLIENT_ID, ""))
                .build();

        Response response = new OkHttpClient().newCall(request).execute();

        String result = response.body().string();

        return parseToken(result);
    }

    private Token parseToken(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String rawToken = jsonObject.getString("access_token");

            String refreshToken;
            if (jsonObject.has("refresh_token")) {
                refreshToken = jsonObject.getString("refresh_token");
            } else {
                refreshToken = "none!";
            }

            int expiryInSeconds = jsonObject.getInt("expires_in");
            return new Token(rawToken, refreshToken, expiryInSeconds, System.currentTimeMillis());
        } catch (JSONException e) {
            throw new RuntimeException("failed to get token", e);
        }
    }

    public Observable<Token> requestUserToken(String redirectUrl) {
        return Observable.just(redirectUrl)
                .map(getAccessToken());
    }

    private Func1<String, Token> getAccessToken() {
        return new Func1<String, Token>() {
            @Override
            public Token call(String redirectUrl) {
                Map<String, List<String>> queryParams = getQueryParams(redirectUrl);

                try {
                    String code = queryParams.get("code").get(0);
                    String uri = REDIRECT_URI;

                    MediaType textMediaType = MediaType.parse("application/x-www-form-urlencoded");
                    Request request = new Request.Builder()
                            .url("https://www.reddit.com/api/v1/access_token")
                            .post(RequestBody.create(textMediaType, "grant_type=authorization_code&code=" + code + "&redirect_uri=" + uri))
                            .addHeader("Authorization", Credentials.basic(CLIENT_ID, ""))
                            .build();

                    Response response = new OkHttpClient().newCall(request).execute();

                    String result = response.body().string();

                    Log.e("!!!", result);

                    return parseToken(result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }
            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

    public Observable<Token> refreshToken(Token token) {
        return Observable.just(token).map(new Func1<Token, Token>() {
            @Override
            public Token call(Token token) {
                Log.e("!!!", " refreshing token");

                try {
                    MediaType textMediaType = MediaType.parse("application/x-www-form-urlencoded");
                    Request request = new Request.Builder()
                            .url("https://www.reddit.com/api/v1/access_token")
                            .post(RequestBody.create(textMediaType, "grant_type=refresh_token&refresh_token=" + token.getRefreshToken()))
                            .addHeader("Authorization", Credentials.basic(CLIENT_ID, ""))
                            .build();

                    Response response = new OkHttpClient().newCall(request).execute();

                    String result = response.body().string();

                    Log.e("!!!", result);

                    return parseToken(result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
