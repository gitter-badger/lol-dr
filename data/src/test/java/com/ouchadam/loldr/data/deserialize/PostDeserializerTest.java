package com.ouchadam.loldr.data.deserialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ouchadam.loldr.data.Data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class PostDeserializerTest {

    private Data.Comments comments;

    @Before
    public void setUp() {
        Class<Data.Comments> type = Data.Comments.class;
        Gson gson = new GsonBuilder().registerTypeAdapter(type, new PostDeserializer()).create();
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("comment.json");
        InputStreamReader reader = new InputStreamReader(resourceAsStream);

        comments = gson.fromJson(reader, type);
    }

    @Test
    public void fooTest() {
        assertThat(filterMoreCommentsOut()).hasSize(100);
    }

    private List<Data.Comment> filterMoreCommentsOut() {
        List<Data.Comment> comments = new ArrayList<>();
        for (Data.Comment comment : this.comments.getComments()) {
            if (!comment.isMore()) {
                comments.add(comment);
            }
        }
        return comments;
    }

}