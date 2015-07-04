package com.ouchadam.loldr.data;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public class Repository {

    private static final String ENDPOINT = "https://oauth.reddit.com/api/v1";
    private final RedditService service;

    public static Repository newInstance(TokenProvider tokenProvider) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new AuthInteceptor(tokenProvider));
        OkClient okClient = new OkClient(okHttpClient);

        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(new GsonBuilder().create()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(okClient)
                .build();

        return new Repository(retrofit.create(RedditService.class));
    }

    Repository(RedditService service) {
        this.service = service;
    }

    public interface RedditService {
        @GET("/me")
        Observable<Post> getMe();

        @GET("/r/{subreddit}/hot")
        Observable<Post> getSubreddit(@Path("subreddit") String subreddit);
    }

    public static class Post {
        @SerializedName("count")
        public int count;
    }

    public Observable<Post> foo() {
        return service.getMe();
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
