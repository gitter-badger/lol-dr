package com.ouchadam.loldr.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenProvider;
import com.ouchadam.auth.UserTokenRequest;
import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.data.Repository;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private MainActivityPresenter mainActivityPresenter;
    private TokenProvider tokenProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityPresenter = MainActivityPresenter.onCreate(this, listener);

        tokenProvider = TokenProvider.newInstance();

        Repository.newInstance(provider).subreddit("all").subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Repository.Feed>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Repository.Feed feed) {
                Toast.makeText(MainActivity.this, "" + feed.getPosts().size(), Toast.LENGTH_LONG).show();
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
