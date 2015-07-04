package com.ouchadam.loldr.feed;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ouchadam.loldr.ui.R;

import java.util.List;

final class Presenter {

    private final PostSummaryAdapter adapter;
    private final Posts postsProvider;

    public static Presenter onCreate(Activity activity) {
        activity.setContentView(R.layout.activity_feed);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.feed_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        Posts postsProvider = Posts.newInstance();
        PostSummaryAdapter adapter = new PostSummaryAdapter(postsProvider, activity.getLayoutInflater());
        recyclerView.setAdapter(adapter);
        return new Presenter(postsProvider, adapter);
    }

    private Presenter(Posts postsProvider, PostSummaryAdapter adapter) {
        this.postsProvider = postsProvider;
        this.adapter = adapter;
    }

    public void present(List<PostSummary> postSummaries) {
        postsProvider.set(postSummaries);
        adapter.notifyDataSetChanged();
    }

}
