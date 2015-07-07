package com.ouchadam.loldr.post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenAcquirer;
import com.ouchadam.auth.UserTokenRequest;
import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.BuildConfig;
import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.data.TokenProvider;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostActivity extends BaseActivity {

    private static final String ACTION = BuildConfig.APPLICATION_ID + ".POST";
    private static final String EXTA_POST_ID = "postId";
    private static final String EXTRA_SUBREDDIT = "subreddit";

    private TokenAcquirer tokenAcquirer;
    private Presenter presenter;

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
        this.presenter = Presenter.onCreate(this, null);

        String subreddit = getIntent().getStringExtra(EXTRA_SUBREDDIT);
        String postId = getIntent().getStringExtra(EXTA_POST_ID);

        Repository.newInstance(provider).comments(subreddit, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(presentResult());
    }

    private Subscriber<Data.Comments> presentResult() {
        return new Subscriber<Data.Comments>() {
            @Override
            public void onCompleted() {
                // do nothing
            }

            @Override
            public void onError(Throwable e) {
                Log.e("!!!", "Something went wrong", e);
            }

            @Override
            public void onNext(Data.Comments comments) {
                List<Data.Comment> dataPosts = comments.getComments();

                presenter.present(toDataSource(dataPosts));
            }
        };
    }

    private DataSource<Comment> toDataSource(final List<Data.Comment> dataPosts) {
        // TODO this will be a cursor !
        return new DataSource<Comment>() {
            @Override
            public int size() {
                return dataPosts.size();
            }

            @Override
            public Comment get(final int position) {
                return new Comment() {
                    @Override
                    public String getId() {
                        return dataPosts.get(position).getId();
                    }

                    @Override
                    public String getBody() {
                        return dataPosts.get(position).getBody();
                    }

                    @Override
                    public String getAuthor() {
                        return dataPosts.get(position).getAuthor();
                    }

                    @Override
                    public int getDepth() {
                        return dataPosts.get(position).getDepth();
                    }

                    @Override
                    public boolean isMore() {
                        return dataPosts.get(position).isMore();
                    }
                };
            }

            @Override
            public void close() {
                dataPosts.clear(); // such a nice guy
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