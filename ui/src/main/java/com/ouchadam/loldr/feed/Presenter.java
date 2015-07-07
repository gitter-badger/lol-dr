package com.ouchadam.loldr.feed;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ouchadam.loldr.ui.R;

import java.util.List;

final class Presenter {

    private final RecyclerView.Adapter adapter;
    private final Posts dataSource;

    static Presenter onCreate(Activity activity, final Listener listener) {
        activity.setContentView(R.layout.activity_feed);

        Posts dataSource = Posts.newInstance();
        final RecyclerView.Adapter adapter = new PostSummaryAdapter(dataSource, activity.getLayoutInflater(), listener);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.feed_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int prevItemCount = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int itemCount = adapter.getItemCount();

                if (prevItemCount != itemCount && lastVisibleItemPosition >= (itemCount - 1) - 15) {
                    prevItemCount = itemCount;
                    listener.onNextPageRequest();
                }
            }
        });

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

    public interface Listener {
        void onPostClicked(PostSummary postSummary);

        void onNextPageRequest();
    }

}
