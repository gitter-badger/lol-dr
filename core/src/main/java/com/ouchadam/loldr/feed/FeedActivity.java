package com.ouchadam.loldr.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenAcquirer;
import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.Executor;
import com.ouchadam.loldr.Ui;
import com.ouchadam.loldr.data.Data;
import com.ouchadam.loldr.data.Repository;
import com.ouchadam.loldr.data.TokenProvider;
import com.ouchadam.loldr.post.PostActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class FeedActivity extends BaseActivity {

    private static final String EXTRA_SUBREDDIT = "subreddit";
    private static final String DEFAULT_SUBREDDIT = "askreddit";

    private final Executor executor;

    private TokenAcquirer tokenAcquirer;
    private Presenter<PostProvider.PostSummarySource> presenter;

    private String afterId;
    private List<Data.Post> cachedPosts = new ArrayList<>();
    private Repository repository;

    private String subreddit;

    public Intent create(String subreddit) {
        Intent intent = new Intent("action");
        intent.putExtra(EXTRA_SUBREDDIT, subreddit);
        return intent;
    }

    public FeedActivity() {
        this.executor = new Executor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tokenAcquirer = TokenAcquirer.newInstance(this);
        PostProvider postProvider = new PostProvider();
        this.presenter = Presenter.onCreate(this, postProvider, listener);

        this.repository = Repository.newInstance(provider);

        this.subreddit = getSubreddit();

        executor.execute(repository.subreddit(subreddit), presentResult());
    }

    private String getSubreddit() {
        return getIntent().hasExtra(EXTRA_SUBREDDIT) ? getIntent().getStringExtra(EXTRA_SUBREDDIT) : DEFAULT_SUBREDDIT;
    }

    private final Presenter.Listener listener = new Presenter.Listener() {
        @Override
        public void onPostClicked(Ui.PostSummary postSummary) {
            startActivity(PostActivity.create(postSummary.getSubreddit(), postSummary.getId()));
        }

        @Override
        public void onNextPageRequest() {
            executor.execute(repository.subreddit(subreddit, afterId), presentResult());
        }
    };

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
                FeedActivity.this.afterId = feed.afterId();
                cachedPosts.addAll(feed.getPosts());               // TODO replace this with a cursor

                List<Ui.PostSummary> summaries = MarshallerFactory.newInstance(getResources()).posts().marshall(cachedPosts);

                presenter.present(new PostProvider.PostSummarySource(summaries));
            }
        };
    }

    private TokenProvider provider = new TokenProvider() {
        @Override
        public TokenProvider.AccessToken provideAccessToken() {
            Token token = tokenAcquirer.acquireToken().toBlocking().first();
            return new TokenProvider.AccessToken(token.getRawToken());
        }
    };

}
