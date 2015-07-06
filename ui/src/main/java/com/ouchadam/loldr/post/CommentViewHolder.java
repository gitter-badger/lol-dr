package com.ouchadam.loldr.post;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ouchadam.loldr.ui.R;

final class CommentViewHolder extends RecyclerView.ViewHolder {

    public static final int POSITION_KEY = R.id.tag_feed_position;

    private final View rootView;
    private final TextView titleView;
    private final TextView timeView;

    static CommentViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater, View.OnClickListener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_post_comment, parent, false);

        TextView bodyView = (TextView) view.findViewById(R.id.post_comment_body);
        TextView authorView = (TextView) view.findViewById(R.id.post_comment_author);

        view.setOnClickListener(postClickListener);

        return new CommentViewHolder(view, bodyView, authorView);
    }

    private CommentViewHolder(View itemView, TextView titleView, TextView timeView) {
        super(itemView);
        this.rootView = itemView;
        this.titleView = titleView;
        this.timeView = timeView;
    }

    public void setBody(String title) {
        titleView.setText(title);
        rootView.setContentDescription(title);
    }

    public void setAuthor(String time) {
        timeView.setText(time);
    }

    public void setPosition(int position) {
        rootView.setTag(POSITION_KEY, position);
    }

}
