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

class PostDeserializer implements JsonDeserializer<Data.Comments> {

    @Override
    public Data.Comments deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject commentsRootJson = json.getAsJsonArray().get(1).getAsJsonObject();
        JsonArray rootCommentThread = commentsRootJson.get("data").getAsJsonObject().get("children").getAsJsonArray();

        List<Data.Comment> comments = new ArrayList<>();
        for (JsonElement jsonElement : rootCommentThread) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            comments.addAll(recurseOverAllComments(asJsonObject, new ArrayList<Comment>()));
        }

        return new Comments(comments);
    }

    private List<Comment> recurseOverAllComments(JsonObject commentThread, List<Comment> comments) {
        String kind = commentThread.get("kind").getAsString();
        if (kind.equals("more")) {
            // TODO these type of comments require a different api call /api/morechildren taking the child ids
            return comments;
        }

        JsonObject commentJson = commentThread.get("data").getAsJsonObject();
        comments.add(parseComment(commentJson));

        JsonElement repliesRoot = commentJson.get("replies");
        if (repliesRoot.isJsonPrimitive()) {
            return comments;
        }

        JsonArray replies = repliesRoot.getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();

        for (JsonElement reply : replies) {
            comments.addAll(recurseOverAllComments(reply.getAsJsonObject(), new ArrayList<Comment>()));
        }

        return comments;
    }

    private Comment parseComment(JsonObject jsonComment) {
        String commentId = jsonComment.get("id").getAsString();
        String commentBody = jsonComment.get("body").getAsString();
        String commentName = jsonComment.get("name").getAsString();
        long commentTimestamp = jsonComment.get("created_utc").getAsLong();
        String commentAuthor = jsonComment.get("author").getAsString();

        return new Comment(commentId, commentBody, commentName, commentTimestamp, commentAuthor);
    }

    public static class Comments implements Data.Comments {

        private final List<Data.Comment> comments;

        public Comments(List<Data.Comment> comments) {
            this.comments = comments;
        }

        @Override
        public List<Data.Comment> getComments() {
            return comments;
        }
    }

    private static class Comment implements Data.Comment {

        private final String id;
        private final String body;
        private final String name;
        private final long createdUtc;
        private final String commentAuthor;

        public Comment(String id, String body, String name, long createdUtc, String commentAuthor) {
            this.id = id;
            this.body = body;
            this.name = name;
            this.createdUtc = createdUtc;
            this.commentAuthor = commentAuthor;
        }

        @Override
        public String getBody() {
            return body;
        }

        @Override
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public long getCreatedUtc() {
            return createdUtc;
        }

        public String getCommentAuthor() {
            return commentAuthor;
        }
    }

}
