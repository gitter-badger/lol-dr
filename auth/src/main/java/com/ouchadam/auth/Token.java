package com.ouchadam.auth;

public class Token {
    private final String urlResponse;

    public Token(String urlResponse) {
        this.urlResponse = urlResponse;
    }

    public String getUrlResponse() {
        return urlResponse;
    }
}
