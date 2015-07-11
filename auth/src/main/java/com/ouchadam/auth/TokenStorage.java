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

    public Token getToken(String userName) {
        if (preferences.contains(userName)) {
            return new Token(
                    preferences.getString(userName + RAW, ""),
                    preferences.getString(userName + REFRESH, ""),
                    preferences.getInt(userName + EXPIRY, -1),
                    preferences.getLong(userName + TIMESTAMP, -1)
            );
        } else {
            return Token.MISSING;
        }
    }

    public void storeToken(String userName, Token token) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("currentUser", userName);

        editor.putBoolean(userName, true);
        editor.putString(userName + RAW, token.getRawToken());
        editor.putString(userName + REFRESH, token.getRefreshToken());
        editor.putInt(userName + EXPIRY, token.getExpiry());
        editor.putLong(userName + TIMESTAMP, token.getTimeStamp());

        editor.apply();
    }

    public Token getCurrentUserToken() {
        Token currentUser = getToken(preferences.getString("currentUser", null));
        return currentUser == null ? Token.MISSING : currentUser;
    }
}
