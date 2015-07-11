package com.ouchadam.loldr.debug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenAcquirer;
import com.ouchadam.loldr.BaseActivity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DebugActivity extends BaseActivity {

    private TokenAcquirer tokenAcquirer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Presenter.onCreate(this, listener);

        tokenAcquirer = TokenAcquirer.newInstance(this);
    }

    private final Presenter.Listener listener = new Presenter.Listener() {
        @Override
        public void onClickUserToken() {
            tokenAcquirer.createUserToken(DebugActivity.this);
        }

        @Override
        public void onClickAnonToken() {
            tokenAcquirer.acquireToken()
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
                            Toast.makeText(DebugActivity.this, token.getRawToken(), Toast.LENGTH_LONG).show();
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

}
