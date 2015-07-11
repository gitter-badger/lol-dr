package com.ouchadam.auth;

import android.content.Context;
import android.content.SharedPreferences;

class AnonTokenStorage {

    private static final String RAW = "raw";
    private static final String EXPIRY = "expiry";

    private final SharedPreferences preferences;

    public static AnonTokenStorage from(Context context) {
        return new AnonTokenStorage(context.getSharedPreferences("com.ouchadam.loldr.token", Context.MODE_PRIVATE));
    }

    private AnonTokenStorage(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void storeToken(AnonToken token) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(RAW, token.getAccessToken());
        editor.putLong(EXPIRY, token.getExpiryTime());

        editor.apply();
    }

    public AnonToken getToken() {
        return new AnonToken(preferences.getString(RAW, ""), preferences.getLong(EXPIRY, -1));
    }
}
