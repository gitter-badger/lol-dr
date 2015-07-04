package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

class PostSummaryAdapter extends RecyclerView.Adapter<PostSummaryViewHolder> {

    private final LayoutInflater layoutInflater;
    private final DataSource<PostSummary> dataSource;

    PostSummaryAdapter(DataSource<PostSummary> dataSource, LayoutInflater layoutInflater) {
        this.dataSource = dataSource;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public PostSummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return PostSummaryViewHolder.inflate(viewGroup, layoutInflater);
    }

    @Override
    public void onBindViewHolder(PostSummaryViewHolder viewHolder, int position) {
        PostSummary postSummary = dataSource.get(position);
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
