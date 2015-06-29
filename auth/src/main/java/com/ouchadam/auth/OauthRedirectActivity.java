package com.ouchadam.auth;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class OauthRedirectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Foo().requestToken(getIntent().getData().toString(), new Foo.Callback() {
            @Override
            public void onSuccess(Foo.Token token) {
                Toast.makeText(OauthRedirectActivity.this, token.getUrlResponse(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
