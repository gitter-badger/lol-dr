package com.ouchadam.loldr.data.deserialize;

import com.google.gson.GsonBuilder;
import com.ouchadam.loldr.data.Data;

public class DeserializerFactory {

    public GsonBuilder feed(GsonBuilder gsonBuilder) {
        return gsonBuilder.registerTypeAdapter(Data.Feed.class, new FeedDeserializer());
    }

}
