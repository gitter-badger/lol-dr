package com.ouchadam.loldr.feed;

import android.database.Cursor;

class PostsCursorDataSource implements CloseableDataSource<PostSummary> {

    private Cursor cursor;

    private PostsCursorDataSource(Cursor cursor) {
        this.cursor = cursor;
    }

    void set(Cursor cursor) {
        closeExistingCursor();
        this.cursor = cursor;
    }

    private void closeExistingCursor() {
        if (cursor != null) {
            close();
        }
    }

    @Override
    public int size() {
        return cursor.getCount();
    }

    @Override
    public PostSummary get(int position) {
        cursor.moveToPosition(position);
        // TODO: marshall cursor row into postSummary
        // but this is the UI module :/
        return new PostSummary() {

            @Override
            public String getId() {
                return "";
            }

            @Override
            public String getTitle() {
                return "";
            }

            @Override
            public String getTime() {
                return "";
            }

            @Override
            public String getSubreddit() {
                return "";
            }

            @Override
            public String getCommentCount() {
                return "";
            }
        };
    }

    @Override
    public void close() {
        cursor.close();
    }

}
