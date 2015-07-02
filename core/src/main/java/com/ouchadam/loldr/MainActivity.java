package com.ouchadam.loldr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenProvider;
import com.ouchadam.auth.UserTokenRequest;
import com.ouchadam.ui.MainActivityPresenter;

public class MainActivity extends AppCompatActivity {

    private MainActivityPresenter mainActivityPresenter;
    private TokenProvider tokenProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityPresenter = MainActivityPresenter.onCreate(this, listener);

        tokenProvider = TokenProvider.newInstance();
    }

    private final MainActivityPresenter.Listener listener = new MainActivityPresenter.Listener() {
        @Override
        public void onClickUserToken() {
            tokenProvider.createUserToken(MainActivity.this);
        }

        @Override
        public void onClickAnonToken() {
            tokenProvider.getToken(UserTokenRequest.anon());
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, data.getStringExtra("data"), Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
