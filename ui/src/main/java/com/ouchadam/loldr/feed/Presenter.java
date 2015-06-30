package com.ouchadam.loldr.feed;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ouchadam.ui.R;

final class Presenter {

    private final PostSummaryAdapter adapter;

    public static Presenter onCreate(Activity activity) {
        activity.setContentView(R.layout.activity_feed);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.feed_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        PostSummaryAdapter adapter = new PostSummaryAdapter(activity.getLayoutInflater());
        recyclerView.setAdapter(adapter);
        return new Presenter(adapter);
    }

    private Presenter(PostSummaryAdapter adapter) {
        this.adapter = adapter;
    }

    public void present() {
        // TODO: adapter.update(Foo) ... what should be the parameter here if we're not passing model to UI?
    }

}
