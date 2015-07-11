package com.ouchadam.auth;

import android.content.Context;
import android.content.SharedPreferences;

class TokenStorage {

    public static final String RAW = "raw";
    public static final String REFRESH = "refresh";
    public static final String EXPIRY = "expiry";
    public static final String TIMESTAMP = "timestamp";
    private final SharedPreferences preferences;

    public static TokenStorage from(Context context) {
        return new TokenStorage(context.getSharedPreferences("com.ouchadam.loldr.token", Context.MODE_PRIVATE));
    }

    private TokenStorage(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public AccessToken getToken(String userName) {
        if (preferences.contains(userName)) {
            return new AccessToken(
                    preferences.getString(userName + RAW, ""),
                    preferences.getString(userName + REFRESH, ""),
                    preferences.getInt(userName + EXPIRY, -1),
                    preferences.getLong(userName + TIMESTAMP, -1)
            );
        } else {
            return AccessToken.MISSING;
        }
    }

    public void storeToken(String userName, AccessToken accessToken) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("currentUser", userName);

        editor.putBoolean(userName, true);
        editor.putString(userName + RAW, accessToken.getRawToken());
        editor.putString(userName + REFRESH, accessToken.getRefreshToken());
        editor.putInt(userName + EXPIRY, accessToken.getExpiry());
        editor.putLong(userName + TIMESTAMP, accessToken.getTimeStamp());

        editor.apply();
    }

    public AccessToken getCurrentUserToken() {
        AccessToken currentUser = getToken(preferences.getString("currentUser", null));
        return currentUser == null ? AccessToken.MISSING : currentUser;
    }
}
