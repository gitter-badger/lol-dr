package com.ouchadam.loldr.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public class Repository {

    private static final String ENDPOINT = "https://oauth.reddit.com/";
    private final RedditService service;

    public static Repository newInstance(TokenProvider tokenProvider) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new AuthInteceptor(tokenProvider));
        OkClient okClient = new OkClient(okHttpClient);

        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(new GsonBuilder().registerTypeAdapter(Feed.class, new FeedDeserializer()).create()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(okClient)
                .build();

        return new Repository(retrofit.create(RedditService.class));
    }

    Repository(RedditService service) {
        this.service = service;
    }

    public interface RedditService {
        @GET("/api/v1/me")
        Observable<Feed> getMe();

        @GET("/r/{subreddit}/hot")
        Observable<Feed> getSubreddit(@Path("subreddit") String subreddit);

        @GET("/")
        Observable<Feed> getFrontPage();
    }

    public static class Feed {

        public final List<Post> posts;

        public Feed(List<Post> posts) {
            this.posts = posts;
        }

        public List<Post> getPosts() {
            return posts;
        }

    }

    public static class Post {

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

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getSubreddit() {
            return subreddit;
        }

        public int getUps() {
            return ups;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public long getCreatedUtcTimeStamp() {
            return createdUtcTimeStamp;
        }
    }

    private static class FeedDeserializer implements JsonDeserializer<Feed> {

        @Override
        public Feed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray postsJson = json.getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();

            List<Post> posts = new ArrayList<>(postsJson.size());

            for (JsonElement postRootJson : postsJson) {
                JsonObject postJson = postRootJson.getAsJsonObject().get("data").getAsJsonObject();

                Post post = new Post(
                        postJson.get("id").getAsString(),
                        postJson.get("title").getAsString(),
                        postJson.get("subreddit").getAsString(),
                        postJson.get("ups").getAsInt(),
                        postJson.get("num_comments").getAsInt(),
                        postJson.get("created_utc").getAsLong()
                );

                posts.add(post);
            }

            return new Feed(posts);
        }
    }

    public Observable<Feed> subreddit(String subreddit) {
        return service.getSubreddit(subreddit);
    }

    public Observable<Feed> frontPage() {
        return service.getFrontPage();
    }

    public interface TokenProvider {
        AccessToken provideAccessToken();
    }

    public static class AccessToken {

        private final String data;

        public AccessToken(String data) {
            this.data = data;
        }

        String get() {
            return data;
        }
    }

}
