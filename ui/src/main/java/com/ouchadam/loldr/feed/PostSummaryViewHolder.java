package com.ouchadam.loldr.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ouchadam.ui.R;

final class PostSummaryViewHolder extends RecyclerView.ViewHolder {

    private final View rootView;
    private final TextView titleView;
    private final TextView timeView;
    private final TextView commentsView;
    private final TextView subredditView;

    static PostSummaryViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.view_feed_post_summary, parent, false);

        TextView titleView = (TextView) view.findViewById(R.id.feed_post_summary_text_title);
        TextView timeView = (TextView) view.findViewById(R.id.feed_post_summary_text_time);
        TextView commentsView = (TextView) view.findViewById(R.id.feed_post_summary_text_comments);
        TextView subredditView = (TextView) view.findViewById(R.id.feed_post_summary_text_subreddit);
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

    public void setCommentsCount(int count) {
        commentsView.setText(String.format("%1$d comment(s)", count));
    }

    public void setSubredditName(String subredditName) {
        subredditView.setText(subredditName);
    }

    public interface PostSummaryClickListener {

        // TODO: need to pass the ID of the post summary that was clicked back
        void onClickPostSummary();

        // TODO: need to pass the ID of the post summary that was clicked back
        void onClickDownloadTogglePostSummary();

    }

}
