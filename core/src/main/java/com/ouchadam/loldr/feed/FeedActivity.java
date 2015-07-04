package com.ouchadam.loldr.feed;

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

public class FeedActivity extends BaseActivity {

    private TokenProvider tokenProvider;
    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenProvider = TokenProvider.newInstance();
        presenter = Presenter.onCreate(this);

        Repository.newInstance(provider).foo()
                .subscribeOn(Schedulers.io())
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
                        Toast.makeText(FeedActivity.this, "" + post.count, Toast.LENGTH_LONG).show();
                    }
                });

        // TODO: presenter.present(List<Posts>)
    }

    private Repository.TokenProvider provider = new Repository.TokenProvider() {
        @Override
        public Repository.AccessToken provideAccessToken() {
            Token token = tokenProvider.getToken(UserTokenRequest.anon()).toBlocking().first();
            return new Repository.AccessToken(token.getUrlResponse());
        }
    };

}
