package com.ouchadam.loldr;

public final class Ui {

    private Ui() {
        throw new IllegalAccessError("Just a holder not a real class");
    }

    public interface PostSummary {

        String getId();

        String getTitle();

        String getTime();

        String getSubreddit();

        String getCommentCount();

    }

    public interface Comment {

        String getId();

        String getBody();

        String getAuthor();

        int getDepth();

        boolean isMore();

    }
}
