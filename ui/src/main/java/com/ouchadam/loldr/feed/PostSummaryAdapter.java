package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

class PostSummaryAdapter extends RecyclerView.Adapter<PostSummaryViewHolder> {

    private final LayoutInflater layoutInflater;
    private final PostsProvider postsProvider;

    PostSummaryAdapter(PostsProvider postsProvider, LayoutInflater layoutInflater) {
        this.postsProvider = postsProvider;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public PostSummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return PostSummaryViewHolder.inflate(viewGroup, layoutInflater);
    }

    @Override
    public void onBindViewHolder(PostSummaryViewHolder viewHolder, int position) {
        Post post = postsProvider.get(position);
        viewHolder.setTitle(post.getTitle());
        viewHolder.setTime(post.getTime());
        viewHolder.setCommentsCount(post.getCommentCount());
        viewHolder.setSubreddit(post.getSubreddit());
    }

    @Override
    public int getItemCount() {
        return postsProvider.size();
    }

}
