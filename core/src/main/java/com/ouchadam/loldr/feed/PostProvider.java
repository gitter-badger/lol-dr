package com.ouchadam.loldr.feed;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.Ui;

import java.util.ArrayList;
import java.util.List;

public class PostProvider implements Presenter.PostSourceProvider<PostProvider.PostSummarySource> {

    private final PostSummarySource postSummarySource = new PostSummarySource();

    @Override
    public void swap(PostSummarySource source) {
        postSummarySource.postSummaries.addAll(source.postSummaries);
    }

    @Override
    public Ui.PostSummary get(int position) {
        return postSummarySource.get(position);
    }

    @Override
    public int size() {
        return postSummarySource.size();
    }

    static class PostSummarySource implements DataSource<Ui.PostSummary> {

        private final List<Ui.PostSummary> postSummaries;

        public PostSummarySource() {
            this(new ArrayList<Ui.PostSummary>());
        }

        public PostSummarySource(List<Ui.PostSummary> postSummaries) {
            this.postSummaries = postSummaries;
        }

        @Override
        public int size() {
            return postSummaries.size();
        }

        @Override
        public Ui.PostSummary get(final int position) {
            return postSummaries.get(position);
        }

    }
}
