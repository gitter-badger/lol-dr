package com.ouchadam.loldr.data;

import java.util.List;

public class Data {

    public interface Post {

        String getId();

        String getTitle();

        String getSubreddit();

        int getUps();

        int getCommentCount();

        long getCreatedUtcTimeStamp();

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