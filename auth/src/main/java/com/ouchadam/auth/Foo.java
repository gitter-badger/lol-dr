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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        String duration = "temporary";
        String scope = "read";

        Intent intent = new Intent(activity, OAuthWebViewActivity.class);
        intent.setData(Uri.parse(BASE_URL
                + CLIENT_ID
                + "&response_type=" + responseType
                + "&state=" + requestId
                + "&redirect_uri=" + REDIRECT_URI
                + "&duration=" + duration
                + "&scope=" + scope));

        activity.startActivityForResult(intent, 100);
    }

    public void requestSignedOutToken(Callback callback) {
        Log.e("!!!", "requestSignedOutToken");

        Observable.just("")
                .map(getSignedOutAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestTokenFromApi(callback));
    }

    private Func1<Object, String> getSignedOutAccessToken() {
        return new Func1<Object, String>() {
            @Override
            public String call(Object s) {
                Log.e("!!!", "running");

                try {
                    MediaType textMediaType = MediaType.parse("application/x-www-form-urlencoded");
                    Request request = new Request.Builder()
                            .url("https://www.reddit.com/api/v1/access_token")
                            .post(RequestBody.create(textMediaType, "grant_type=https://oauth.reddit.com/grants/installed_client&device_id=" + uniqueDeviceId.toString()))
                            .addHeader("Authorization", Credentials.basic(CLIENT_ID, ""))
                            .build();

                    Response response = new OkHttpClient().newCall(request).execute();

                    Log.e("!!!", "sending : " + request.urlString());

                    return response.body().string();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public void requestToken(String redirectUrl, Callback callback) {
        Observable.just(redirectUrl)
                .map(getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestTokenFromApi(callback));
    }

    private Func1<String, String> getAccessToken() {
        return new Func1<String, String>() {
            @Override
            public String call(String s) {
                Map<String, List<String>> queryParams = getQueryParams(s);

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

                    Log.e("!!!", "sending : " + request.urlString());
                    Log.e("!!!", "code : " + code);

                    return response.body().string();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public static Map<String, List<String>> getQueryParams(String url) {
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

    private Subscriber<String> requestTokenFromApi(final Callback callback) {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("!!!", "error", e);
            }

            @Override
            public void onNext(String url) {
                Log.e("!!!", "Got a valid result! : " + url);
                callback.onSuccess(new Token(url));
            }
        };
    }

    public interface Callback {
        void onSuccess(Token token);
    }

}
