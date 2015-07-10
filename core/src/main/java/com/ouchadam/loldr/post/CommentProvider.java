package com.ouchadam.loldr.post;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.data.Data;

import java.util.ArrayList;
import java.util.List;

public class CommentProvider implements SourceProvider<Comment, CommentProvider.CommentSource> {

    private CommentSource commentSource = new CommentSource();

    @Override
    public void swap(CommentSource source) {
        commentSource.dataPosts.addAll(source.dataPosts);
    }

    @Override
    public Comment get(int position) {
        return commentSource.get(position);
    }

    @Override
    public int size() {
        return commentSource.size();
    }

    static class CommentSource implements DataSource<Comment> {

        private final List<Data.Comment> dataPosts;

        private CommentSource() {
            this(new ArrayList<Data.Comment>());
        }

        CommentSource(List<Data.Comment> dataPosts) {
            this.dataPosts = dataPosts;
        }

        @Override
        public int size() {
            return dataPosts.size();
        }

        @Override
        public Comment get(final int position) {
            return new Comment() {
                @Override
                public String getId() {
                    return dataPosts.get(position).getId();
                }

                @Override
                public String getBody() {
                    return dataPosts.get(position).getBody();
                }

                @Override
                public String getAuthor() {
                    return dataPosts.get(position).getAuthor();
                }

                @Override
                public int getDepth() {
                    return dataPosts.get(position).getDepth();
                }

                @Override
                public boolean isMore() {
                    return dataPosts.get(position).isMore();
                }
            };
        }

    }

}
