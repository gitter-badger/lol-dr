package com.ouchadam.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OAuthWebViewActivity extends Activity {

    public static final String REDIRECT_URI = "http://com.ouchadam.kanto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_web);
        WebView webView = (WebView) findViewById(R.id.web);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (shouldOverride(url)) {
                    onRedirect(url);
                    // TODO hide and show spinner
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.loadUrl(getIntent().getData().toString());
    }

    private boolean shouldOverride(String url) {
        return url.startsWith(REDIRECT_URI);
    }

    private void onRedirect(String redirectUrl) {
        UserTokenRequest userTokenRequest = new UserTokenRequest(UserTokenRequest.Type.SIGNED_IN, redirectUrl);
        TokenProvider.newInstance().getToken(userTokenRequest, new TokenProvider.Callback() {
            @Override
            public void onTokenAcquired(Token token) {
                Intent data = new Intent();
                data.putExtra("data", token.getUrlResponse());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
