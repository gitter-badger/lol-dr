package com.ouchadam.auth;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

class UserFetcher {

    private static final String ENDPOINT = "https://oauth.reddit.com/";

    String fetchUserName(TokenResponse tokenResponse) {
        try {
            Request request = new Request.Builder()
                    .url(ENDPOINT + "/api/v1/me")
                    .addHeader("Authorization", "bearer " + tokenResponse.getRawToken())
                    .get()
                    .build();

            String result = new OkHttpClient().newCall(request).execute().body().string();
            return parseName(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String parseName(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getString("name");
        } catch (JSONException e) {
            throw new RuntimeException("failed to get user name", e);
        }
    }

}
