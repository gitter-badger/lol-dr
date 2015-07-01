package com.ouchadam.auth;

public class UserTokenRequest {

    private final Type type;
    private final String redirectUrl;

    public static UserTokenRequest anon() {
        return new UserTokenRequest(Type.ANON, "");
    }

    public UserTokenRequest(Type type, String redirectUrl) {
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
