package com.ouchadam.loldr.data.deserialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ouchadam.loldr.data.Data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SubscriptionsDefaultDeserializerTest {

    private Data.Subscriptions subscriptions;

    @Before
    public void setUp() {
        Class<Data.Subscriptions> type = Data.Subscriptions.class;
        Gson gson = new GsonBuilder().registerTypeAdapter(type, new SubscriptionsDeserializer()).create();
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("subreddits_default.json");
        InputStreamReader reader = new InputStreamReader(resourceAsStream);

        subscriptions = gson.fromJson(reader, type);
    }

    @Test
    public void fooTest() {
        assertThat(subscriptions.getSubscribedSubreddits()).hasSize(50);
    }

    @Test
    public void fooTest2() {
        List<Data.Subreddit> subscribedSubreddits = subscriptions.getSubscribedSubreddits();

        Data.Subreddit subreddit = subscribedSubreddits.get(0);

        assertThat(subreddit.getId()).isEqualTo("2qgzt");
    }

    @Test
    public void fooTest3() {
        List<Data.Subreddit> subscribedSubreddits = subscriptions.getSubscribedSubreddits();

        Data.Subreddit subreddit = subscribedSubreddits.get(0);

        assertThat(subreddit.getName()).isEqualTo("gadgets");
    }

}