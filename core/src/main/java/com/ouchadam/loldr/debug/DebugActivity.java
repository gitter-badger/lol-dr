package com.ouchadam.loldr.debug;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenAcquirer;
import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.UserTokenProvider;
import com.ouchadam.loldr.R;
import com.ouchadam.loldr.feed.FeedActivity;

import java.io.IOException;

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
            // TODO use the account manager!

            AccountManager.get(DebugActivity.this).addAccount(getResources().getString(R.string.account_type), null, null, null, DebugActivity.this, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> accountManagerFuture) {

                    try {
                        Bundle result = accountManagerFuture.getResult();

                        String accountName = result.getString(AccountManager.KEY_ACCOUNT_NAME);

                        Log.e("!!!", "added account : " + accountName);

                        UserTokenProvider.PreferenceUserProvider.newInstance(DebugActivity.this).updateCurrentUser(accountName);

                        finish();
                    } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                        e.printStackTrace();
                    }

                }
            }, new Handler());

        }

        @Override
        public void onClickAnonToken() {
            tokenAcquirer.acquireToken(null)
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
                            Toast.makeText(DebugActivity.this, token.getAccessToken(), Toast.LENGTH_LONG).show();
                        }
                    });
        }

        @Override
        public void onClickNavigateToFeed() {
            startActivity(new Intent(DebugActivity.this, FeedActivity.class));
            finish();
        }
    };

}
