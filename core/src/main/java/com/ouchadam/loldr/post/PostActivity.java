package com.ouchadam.loldr.post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenAcquirer;
import com.ouchadam.auth.UserTokenRequest;
import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.data.TokenProvider;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostActivity extends BaseActivity {

    private static final String ACTION = "action";
    private static final String EXTA_POST_ID = "postId";
    private static final String EXTRA_SUBREDDIT = "subreddit";

    private TokenAcquirer tokenAcquirer;

    public static Intent create(String subreddit, String postId) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_SUBREDDIT, subreddit);
        intent.putExtra(EXTA_POST_ID, postId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tokenAcquirer = TokenAcquirer.newInstance();

        Repository.newInstance(provider).subreddit("askreddit")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(presentResult());
    }

    private Subscriber<Data.Feed> presentResult() {
        return new Subscriber<Data.Feed>() {
            @Override
            public void onCompleted() {
                // do nothing
            }

            @Override
            public void onError(Throwable e) {
                Log.e("!!!", "Something went wrong", e);
            }

            @Override
            public void onNext(Data.Feed feed) {
                List<Data.Post> dataPosts = feed.getPosts();

                // TODO present the data!
            }
        };
    }

    private TokenProvider provider = new TokenProvider() {
        @Override
        public TokenProvider.AccessToken provideAccessToken() {
            Token token = tokenAcquirer.acquireToken(UserTokenRequest.anon()).toBlocking().first();
            return new TokenProvider.AccessToken(token.getUrlResponse());
        }
    };

}
