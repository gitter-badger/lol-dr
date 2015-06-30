package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

class PostSummaryAdapter extends RecyclerView.Adapter<PostSummaryViewHolder> {

    private final LayoutInflater layoutInflater;

    PostSummaryAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void update() {
        // TODO: update wat
        notifyDataSetChanged();
    }

    @Override
    public PostSummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return PostSummaryViewHolder.inflate(viewGroup, layoutInflater);
    }

    @Override
    public void onBindViewHolder(PostSummaryViewHolder viewHolder, int position) {
        // TODO: bind things
    }

    @Override
    public int getItemCount() {
        // TODO: return correct size
        return 0;
    }

}
