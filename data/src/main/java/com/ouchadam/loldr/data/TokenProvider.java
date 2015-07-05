package com.ouchadam.loldr.data;

public interface TokenProvider {
    AccessToken provideAccessToken();

    class AccessToken {

        private final String data;

        public AccessToken(String data) {
            this.data = data;
        }

        String get() {
            return data;
        }
    }
}
