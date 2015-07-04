package com.ouchadam.loldr.feed;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ouchadam.loldr.ui.R;

import java.util.List;

final class Presenter {

    private final RecyclerView.Adapter adapter;
    private final Posts dataSource;

    static Presenter onCreate(Activity activity) {
        activity.setContentView(R.layout.activity_feed);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.feed_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        Posts dataSource = Posts.newInstance();
        RecyclerView.Adapter adapter = new PostSummaryAdapter(dataSource, activity.getLayoutInflater());
        recyclerView.setAdapter(adapter);
        return new Presenter(dataSource, adapter);
    }

    private Presenter(Posts dataSource, RecyclerView.Adapter adapter) {
        this.dataSource = dataSource;
        this.adapter = adapter;
    }

    public void present(List<PostSummary> postSummaries) {
        dataSource.set(postSummaries);
        adapter.notifyDataSetChanged();
    }

}
