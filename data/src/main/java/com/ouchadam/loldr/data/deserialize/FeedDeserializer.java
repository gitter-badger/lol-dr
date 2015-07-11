package com.ouchadam.loldr.data.deserialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ouchadam.loldr.data.Data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class FeedDeserializer implements JsonDeserializer<Data.Feed> {

    @Override
    public Data.Feed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject dataJson = json.getAsJsonObject().get("data").getAsJsonObject();
        String afterId = dataJson.get("after").getAsString();
        JsonArray postsJson = dataJson.get("children").getAsJsonArray();

        List<Data.Post> posts = new ArrayList<>(postsJson.size());

        for (JsonElement postRootJson : postsJson) {
            JsonObject postJson = postRootJson.getAsJsonObject().get("data").getAsJsonObject();

            Data.Post post = new Post(
                    postJson.get("id").getAsString(),
                    postJson.get("title").getAsString(),
                    postJson.get("subreddit").getAsString(),
                    postJson.get("ups").getAsInt(),
                    postJson.get("num_comments").getAsInt(),
                    postJson.get("created_utc").getAsLong()
            );

            posts.add(post);
        }

        return new Feed(posts, afterId);
    }

    private static class Post implements Data.Post {

        private final String id;
        private final String title;
        private final String subreddit;
        private final int ups;
        private final int commentCount;
        private final long createdUtcTimeStamp;

        public Post(String id, String title, String subreddit, int ups, int commentCount, long createdUtcTimeStamp) {
            this.id = id;
            this.title = title;
            this.subreddit = subreddit;
            this.ups = ups;
            this.commentCount = commentCount;
            this.createdUtcTimeStamp = createdUtcTimeStamp;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getSubreddit() {
            return subreddit;
        }

        @Override
        public int getUps() {
            return ups;
        }

        @Override
        public int getCommentCount() {
            return commentCount;
        }

        @Override
        public long getCreatedDate() {
            return createdUtcTimeStamp;
        }
    }

    private static class Feed implements Data.Feed {

        private final List<Data.Post> posts;
        private final String afterId;

        private Feed(List<Data.Post> posts, String afterId) {
            this.posts = posts;
            this.afterId = afterId;
        }

        @Override
        public List<Data.Post> getPosts() {
            return posts;
        }

        @Override
        public String afterId() {
            return afterId;
        }
    }
}
