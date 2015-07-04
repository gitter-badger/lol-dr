package com.ouchadam.loldr.feed;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenProvider;
import com.ouchadam.auth.UserTokenRequest;
import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.data.Repository;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FeedActivity extends BaseActivity {

    private TokenProvider tokenProvider;
    private Presenter presenter;
    private MarshallerFactory marshallerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.marshallerFactory = new MarshallerFactory();
        this.tokenProvider = TokenProvider.newInstance();
        this.presenter = Presenter.onCreate(this);

        Repository.newInstance(provider).frontPage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(presentResult());
    }

    private Subscriber<Repository.Feed> presentResult() {
        return new Subscriber<Repository.Feed>() {
            @Override
            public void onCompleted() {
                // do nothing
            }

            @Override
            public void onError(Throwable e) {
                Log.e("!!!", "Something went wrong", e);
            }

            @Override
            public void onNext(Repository.Feed feed) {
                Toast.makeText(FeedActivity.this, "" + feed.getPosts().size(), Toast.LENGTH_LONG).show();

                List<PostSummary> postSummaries = marshallerFactory.posts().marshall(feed.getPosts());

                presenter.present(postSummaries);

            }
        };
    }

    private Repository.TokenProvider provider = new Repository.TokenProvider() {
        @Override
        public Repository.AccessToken provideAccessToken() {
            Token token = tokenProvider.getToken(UserTokenRequest.anon()).toBlocking().first();
            return new Repository.AccessToken(token.getUrlResponse());
        }
    };

}
