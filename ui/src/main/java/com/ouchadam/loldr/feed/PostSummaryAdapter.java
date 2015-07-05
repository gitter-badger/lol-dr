package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class PostSummaryAdapter extends RecyclerView.Adapter<PostSummaryViewHolder> {

    private final LayoutInflater layoutInflater;
    private final DataSource<PostSummary> dataSource;
    private final Presenter.Listener listener;

    PostSummaryAdapter(DataSource<PostSummary> dataSource, LayoutInflater layoutInflater, Presenter.Listener listener) {
        this.dataSource = dataSource;
        this.layoutInflater = layoutInflater;
        this.listener = listener;
    }

    @Override
    public PostSummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return PostSummaryViewHolder.inflate(viewGroup, layoutInflater, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostSummary postSummary = dataSource.get((Integer) view.getTag(PostSummaryViewHolder.POSITION_KEY));
                listener.onPostClicked(postSummary);
            }
        });
    }

    @Override
    public void onBindViewHolder(PostSummaryViewHolder viewHolder, int position) {
        PostSummary postSummary = dataSource.get(position);

        viewHolder.setPosition(position);
        viewHolder.setTitle(postSummary.getTitle());
        viewHolder.setTime(postSummary.getTime());
        viewHolder.setCommentsCount(postSummary.getCommentCount());
        viewHolder.setSubreddit(postSummary.getSubreddit());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

}
