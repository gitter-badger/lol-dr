package com.ouchadam.loldr.data.deserialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ouchadam.loldr.data.Data;

public class DeserializerFactory {

    public Gson create() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        feed(gsonBuilder);
        comments(gsonBuilder);
        return gsonBuilder.create();
    }

    private GsonBuilder feed(GsonBuilder gsonBuilder) {
        return gsonBuilder.registerTypeAdapter(Data.Feed.class, new FeedDeserializer());
    }

    private GsonBuilder comments(GsonBuilder gsonBuilder) {
        return gsonBuilder.registerTypeAdapter(Data.Comments.class, new PostDeserializer());
    }

    private GsonBuilder subscriptions(GsonBuilder gsonBuilder) {
        return gsonBuilder.registerTypeAdapter(Data.Comments.class, new SubscriptionsDeserializer());
    }

}
