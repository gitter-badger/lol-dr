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

public class SubscriptionsDeserializer implements JsonDeserializer<Data.Subscriptions> {

    @Override
    public Data.Subscriptions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonArray children = json.getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();

        List<Data.Subreddit> subreddits = new ArrayList<>(children.size());

        for (JsonElement child : children) {
            JsonObject jsonSubreddit = child.getAsJsonObject().get("data").getAsJsonObject();
            subreddits.add(parseSubredditJson(jsonSubreddit));
        }

        return new Subscriptions(subreddits);
    }

    private Data.Subreddit parseSubredditJson(JsonObject jsonSubreddit) {
        String id = jsonSubreddit.get("id").getAsString();
        String name = jsonSubreddit.get("display_name").getAsString();
        return new Subreddit(id, name);
    }

    private static class Subscriptions implements Data.Subscriptions {

        private final List<Data.Subreddit> subreddits;

        public Subscriptions(List<Data.Subreddit> subreddits) {
            this.subreddits = subreddits;
        }

        @Override
        public List<Data.Subreddit> getSubscribedSubreddits() {
            return subreddits;
        }
    }

    private static class Subreddit implements Data.Subreddit {

        private final String id;
        private final String name;

        private Subreddit(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

    }

}
