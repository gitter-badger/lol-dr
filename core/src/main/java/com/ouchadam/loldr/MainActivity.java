package com.ouchadam.loldr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ouchadam.auth.TokenProvider;
import com.ouchadam.ui.MainActivityPresenter;

public class MainActivity extends AppCompatActivity {

    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityPresenter = MainActivityPresenter.onCreate(this);

        TokenProvider tokenProvider = TokenProvider.newInstance();
        tokenProvider.createUserToken(this);

//
//        tokenProvider.getToken(User.anon(), new TokenProvider.Callback() {
//            @Override
//            public void onTokenAcquired(Token token) {
//                Toast.makeText(MainActivity.this, token.getUrlResponse(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

}
