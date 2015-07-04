package com.ouchadam.loldr.data;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import rx.Observable;

public class Repository {

    private final TokenProvider tokenProvider;

    public Repository(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public interface GitHubService {
        @GET("/r/{subreddit}/me")
        Observable<Post> getSubreddit();
//        Observable<Post> getSubreddit(@Path("subreddit") String subreddit);
    }

    public static class Post {
        @SerializedName("count")
        public int count;
    }

    public Observable<Post> foo() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request compressedRequest = originalRequest.newBuilder()
                        .header("Authorization", "bearer " + tokenProvider.provideAccessToken())
                        .method(originalRequest.method(), originalRequest.body())
                        .build();
                return chain.proceed(compressedRequest);
            }
        });
        OkClient okClient = new OkClient(okHttpClient);


        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint("https://oauth.reddit.com/api/v1")
                .setConverter(new GsonConverter(new GsonBuilder().create()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(okClient)
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        return service.getSubreddit();
    }

    public interface TokenProvider {
        AccessToken provideAccessToken();
    }

    public static class AccessToken {

        private final String data;

        public AccessToken(String data) {
            this.data = data;
        }

        String data() {
            return data;
        }
    }

}
