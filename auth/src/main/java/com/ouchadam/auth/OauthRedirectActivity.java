package com.ouchadam.auth;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class OAuthRedirectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String redirectUrl = getIntent().getData().toString();
        TokenProvider.newInstance().getToken(new UserTokenRequest(UserTokenRequest.Type.SIGNED_IN, redirectUrl), new TokenProvider.Callback() {
            @Override
            public void onTokenAcquired(Token token) {
                Toast.makeText(OAuthRedirectActivity.this, token.getUrlResponse(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class TokenPersister {

        public void persistToken(Token token) {

        }

    }
}
