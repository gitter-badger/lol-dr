package com.ouchadam.auth;

public class User {

    private final Type type;
    private final String redirectUrl;

    public static User anon() {
        return new User(Type.ANON, "");
    }

    public User(Type type, String redirectUrl) {
        this.type = type;
        this.redirectUrl = redirectUrl;
    }

    public Type getType() {
        return type;
    }

    public String getCode() {
        return redirectUrl;
    }

    public enum Type {
        ANON,
        SIGNED_IN;
    }

}
