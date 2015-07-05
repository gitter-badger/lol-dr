package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ouchadam.loldr.ui.R;

final class PostSummaryViewHolder extends RecyclerView.ViewHolder {

    public static final int POSITION_KEY = R.id.tag_feed_position;

    private final View rootView;
    private final TextView titleView;
    private final TextView timeView;
    private final TextView commentsView;
    private final TextView subredditView;

    static PostSummaryViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater, View.OnClickListener postClickListener) {
        View view = layoutInflater.inflate(R.layout.view_feed_post_summary, parent, false);

        TextView titleView = (TextView) view.findViewById(R.id.feed_post_summary_text_title);
        TextView timeView = (TextView) view.findViewById(R.id.feed_post_summary_text_time);
        TextView commentsView = (TextView) view.findViewById(R.id.feed_post_summary_text_comments);
        TextView subredditView = (TextView) view.findViewById(R.id.feed_post_summary_text_subreddit);

        view.setOnClickListener(postClickListener);

        return new PostSummaryViewHolder(view, titleView, timeView, commentsView, subredditView);
    }

    private PostSummaryViewHolder(View itemView, TextView titleView, TextView timeView, TextView commentsView,
                                  TextView subredditView) {
        super(itemView);
        this.rootView = itemView;
        this.titleView = titleView;
        this.timeView = timeView;
        this.commentsView = commentsView;
        this.subredditView = subredditView;
    }

    public void setTitle(String title) {
        titleView.setText(title);
        rootView.setContentDescription(title);
    }

    public void setTime(String time) {
        timeView.setText(time);
    }

    public void setCommentsCount(String count) {
        commentsView.setText(count);
    }

    public void setSubreddit(String subredditName) {
        subredditView.setText(subredditName);
    }

    public void setPosition(int position) {
        rootView.setTag(POSITION_KEY, position);
    }

    public interface PostInteractionsListener {

        void onClick(PostSummary postSummary);

    }

}
