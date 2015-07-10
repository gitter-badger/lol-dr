package com.ouchadam.loldr.feed;

import com.ouchadam.loldr.DataSource;

import java.util.ArrayList;
import java.util.List;

public class PostProvider implements Presenter.PostSourceProvider<PostProvider.PostSummarySource> {

    private final PostSummarySource postSummarySource = new PostSummarySource();

    @Override
    public void swap(PostSummarySource source) {
        postSummarySource.postSummaries.addAll(source.postSummaries);
    }

    @Override
    public PostSummary get(int position) {
        return postSummarySource.get(position);
    }

    @Override
    public int size() {
        return postSummarySource.size();
    }

    static class PostSummarySource implements DataSource<PostSummary> {

        private final List<PostSummary> postSummaries;

        public PostSummarySource() {
            this(new ArrayList<PostSummary>());
        }

        public PostSummarySource(List<PostSummary> postSummaries) {
            this.postSummaries = postSummaries;
        }

        @Override
        public int size() {
            return postSummaries.size();
        }

        @Override
        public PostSummary get(final int position) {
            return postSummaries.get(position);
        }

    }
}
