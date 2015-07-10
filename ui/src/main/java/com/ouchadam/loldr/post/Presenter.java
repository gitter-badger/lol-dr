package com.ouchadam.loldr.post;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.ui.R;

public class Presenter<T extends DataSource<Comment>> {

    private final CommentAdapter<T> adapter;

    static <T extends DataSource<Comment>> Presenter<T> onCreate(Activity activity, SourceProvider<Comment, T> dataSource, Listener listener) {
        activity.setContentView(R.layout.activity_post);

        CommentAdapter<T> adapter = new CommentAdapter<>(dataSource, activity.getLayoutInflater(), listener);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.comment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        return new Presenter<>(adapter);
    }

    private Presenter(CommentAdapter<T> adapter) {
        this.adapter = adapter;
    }

    public void present(T dataSource) {
        adapter.notifyDataSourceChanged(dataSource);
    }

    public interface Listener {
        void onCommentClicked(Comment comment);
    }

}
