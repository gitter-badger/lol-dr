package com.ouchadam.loldr.data.deserialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ouchadam.loldr.data.Data;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class PostDeserializerTest {

    private PostDeserializer.Comments comments;

    @Before
    public void setUp() {
        Class<PostDeserializer.Comments> type = PostDeserializer.Comments.class;
        Gson gson = new GsonBuilder().registerTypeAdapter(type, new PostDeserializer()).create();
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("comment.json");
        InputStreamReader reader = new InputStreamReader(resourceAsStream);

        comments = gson.fromJson(reader, type);
    }

    @Test
    public void fooTest() {
        for (Data.Comment comment : comments.getComments()) {
            System.out.println(comment.getBody() + " " + comment.getId());
        }

        assertThat(this.comments.getComments()).hasSize(100);
    }

}