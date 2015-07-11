package com.ouchadam.loldr.data;

import java.util.List;

public final class Data {

    private Data() {
        throw new IllegalAccessError("Just a holder not a real class");
    }

    public interface Post {

        String getId();

        String getTitle();

        String getSubreddit();

        int getUps();

        int getCommentCount();

        long getCreatedDate();

    }

    public interface Feed {

        List<Data.Post> getPosts();

        String afterId();

    }

    public interface Comment {

        String getId();

        String getBody();

        String getAuthor();

        int getDepth();

        boolean isMore();
    }

    public interface Comments {

        List<Data.Comment> getComments();

    }
}
