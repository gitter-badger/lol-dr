package com.ouchadam.loldr.feed;

import java.util.ArrayList;
import java.util.List;

class Posts implements PostsProvider {

    private final List<Post> posts;

    static Posts newInstance() {
        return new Posts(new ArrayList<Post>());
    }

    private Posts(List<Post> posts) {
        this.posts = posts;
    }

    public void set(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
    }

    @Override
    public int size() {
        return posts.size();
    }

    @Override
    public Post get(int position) {
        return posts.get(position);
    }

}
