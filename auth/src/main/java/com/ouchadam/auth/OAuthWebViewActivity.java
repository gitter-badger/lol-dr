package com.ouchadam.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        TokenAcquirer.newInstance(OAuthWebViewActivity.this).requestNewToken(redirectUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Token>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Token token) {
                        Intent data = new Intent();
                        data.putExtra("data", token.getRawToken());
                        setResult(RESULT_OK, data);
                        finish();
                    }
                });
    }

}
