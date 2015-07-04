package com.ouchadam.loldr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenProvider;
import com.ouchadam.auth.UserTokenRequest;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.ui.MainActivityPresenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private MainActivityPresenter mainActivityPresenter;
    private TokenProvider tokenProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityPresenter = MainActivityPresenter.onCreate(this, listener);

        tokenProvider = TokenProvider.newInstance();

        new Repository(provider).foo().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Repository.Post>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Repository.Post post) {
                Toast.makeText(MainActivity.this, "" + post.count, Toast.LENGTH_LONG).show();
            }
        });

    }

    private final MainActivityPresenter.Listener listener = new MainActivityPresenter.Listener() {
        @Override
        public void onClickUserToken() {
            tokenProvider.createUserToken(MainActivity.this);
        }

        @Override
        public void onClickAnonToken() {
            tokenProvider.getToken(UserTokenRequest.anon())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Token>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Token token) {
                            Toast.makeText(MainActivity.this, token.getUrlResponse(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, data.getStringExtra("data"), Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    Repository.TokenProvider provider = new Repository.TokenProvider() {
        @Override
        public Repository.AccessToken provideAccessToken() {
            Token token = tokenProvider.getToken(UserTokenRequest.anon()).toBlocking().first();
            return new Repository.AccessToken(token.getUrlResponse());
        }
    };


}
