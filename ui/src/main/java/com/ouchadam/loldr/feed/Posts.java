package com.ouchadam.loldr.feed;

import java.util.ArrayList;
import java.util.List;

class Posts implements DataSource<PostSummary> {

    private final List<PostSummary> postSummaries;

    static Posts newInstance() {
        return new Posts(new ArrayList<PostSummary>());
    }

    private Posts(List<PostSummary> postSummaries) {
        this.postSummaries = postSummaries;
    }

    public void set(List<PostSummary> postSummaries) {
        this.postSummaries.clear();
        this.postSummaries.addAll(postSummaries);
    }

    @Override
    public int size() {
        return postSummaries.size();
    }

    @Override
    public PostSummary get(int position) {
        return postSummaries.get(position);
    }

}
