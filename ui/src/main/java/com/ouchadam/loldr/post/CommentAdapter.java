package com.ouchadam.loldr.post;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.post.Presenter.Listener;

class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Listener listener;

    private DataSource<Comment> dataSource;

    CommentAdapter(DataSource<Comment> dataSource, LayoutInflater layoutInflater, Listener listener) {
        this.dataSource = dataSource;
        this.layoutInflater = layoutInflater;
        this.listener = listener;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == CommentViewHolder.VIEW_TYPE_COMMENT) {
            return CommentViewHolder.inflateComment(viewGroup, layoutInflater, getPostClickListener());
        }
        return CommentViewHolder.inflateMore(viewGroup, layoutInflater, getPostClickListener());
    }

    private View.OnClickListener getPostClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = dataSource.get((Integer) view.getTag(CommentViewHolder.POSITION_KEY));
                listener.onCommentClicked(comment);
            }
        };
    }

    @Override
    public void onBindViewHolder(CommentViewHolder viewHolder, int position) {
        Comment comment = dataSource.get(position);

        viewHolder.setPosition(position);
        viewHolder.setDepth(comment.getDepth());

        if (!comment.isMore()) {
            viewHolder.setBody(comment.getBody());
            viewHolder.setAuthor(comment.getAuthor());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataSource.get(position).isMore() ? CommentViewHolder.VIEW_TYPE_MORE : CommentViewHolder.VIEW_TYPE_COMMENT;
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void notifyDataSourceChanged(DataSource<Comment> dataSource) {
        this.dataSource.close();
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }
}
