package com.ouchadam.loldr.post;

public interface Comment {

    String getId();

    String getBody();

    String getAuthor();

    int getDepth();

    boolean isMore();

}
