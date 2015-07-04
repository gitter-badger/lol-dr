package com.ouchadam.loldr.feed;

import android.os.Bundle;
import android.widget.Toast;

import com.ouchadam.auth.Token;
import com.ouchadam.auth.TokenProvider;
import com.ouchadam.auth.UserTokenRequest;
import com.ouchadam.loldr.BaseActivity;
import com.ouchadam.loldr.data.Repository;

import java.util.ArrayList;
import java.util.List;

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

        Repository.newInstance(provider).frontPage()
                .subscribeOn(Schedulers.io())
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
                        Toast.makeText(FeedActivity.this, "" + feed.getPosts().size(), Toast.LENGTH_LONG).show();

                        List<Post> uiPosts = marshallToUi(feed);

                        presenter.present(uiPosts);

                    }
                });

        // TODO: presenter.present(List<Posts>)
    }

    private List<Post> marshallToUi(Repository.Feed feed) {
        List<Repository.Post> dataPosts = feed.getPosts();
        List<Post> posts = new ArrayList<>(dataPosts.size());

        for (final Repository.Post dataPost : dataPosts) {
            posts.add(new Post() {
                @Override
                public String getId() {
                    return dataPost.getId();
                }

                @Override
                public String getTitle() {
                    return dataPost.getTitle();
                }

                @Override
                public String getTime() {
                    return String.valueOf(dataPost.getCreatedUtcTimeStamp());
                }

                @Override
                public String getSubreddit() {
                    return dataPost.getSubreddit();
                }

                @Override
                public String getCommentCount() {
                    return String.valueOf(dataPost.getCommentCount());
                }
            });
        }

        return posts;
    }

    private Repository.TokenProvider provider = new Repository.TokenProvider() {
        @Override
        public Repository.AccessToken provideAccessToken() {
            Token token = tokenProvider.getToken(UserTokenRequest.anon()).toBlocking().first();
            return new Repository.AccessToken(token.getUrlResponse());
        }
    };

}
