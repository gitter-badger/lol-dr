package com.ouchadam.auth;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class OauthRedirectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String redirectUrl = getIntent().getData().toString();
        TokenProvider.newInstance().getToken(new User(User.Type.SIGNED_IN, redirectUrl), new TokenProvider.Callback() {
            @Override
            public void onTokenAcquired(Token token) {
                Toast.makeText(OauthRedirectActivity.this, token.getUrlResponse(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
