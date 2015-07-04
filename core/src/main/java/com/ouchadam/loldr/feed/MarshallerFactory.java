package com.ouchadam.loldr.feed;

import com.ouchadam.loldr.data.Repository;

import java.util.ArrayList;
import java.util.List;

class MarshallerFactory {

    public Marshaller<List<PostSummary>, List<Repository.Post>> posts() {
       return marshallList(post());
    }

    private <T, F> Marshaller<List<T>, List<F>> marshallList(final Marshaller<T, F> marshaller) {
        return new Marshaller<List<T>, List<F>>() {
            @Override
            public List<T> marshall(List<F> from) {
                List<T> marshalledItems = new ArrayList<>(from.size());
                for (F f : from) {
                    marshalledItems.add(marshaller.marshall(f));
                }
                return marshalledItems;
            }
        };
    }

    private Marshaller<PostSummary, Repository.Post> post() {
        return new Marshaller<PostSummary, Repository.Post>() {
            @Override
            public PostSummary marshall(final Repository.Post from) {
                return new PostSummary() {
                    @Override
                    public String getId() {
                        return from.getId();
                    }

                    @Override
                    public String getTitle() {
                        return from.getTitle();
                    }

                    @Override
                    public String getTime() {
                        return String.valueOf(from.getCreatedUtcTimeStamp());
                    }

                    @Override
                    public String getSubreddit() {
                        return from.getSubreddit();
                    }

                    @Override
                    public String getCommentCount() {
                        return String.valueOf(from.getCommentCount());
                    }
                };
            }
        };
    }

    public interface Marshaller<T, F> {
        T marshall(F from);
    }

}
