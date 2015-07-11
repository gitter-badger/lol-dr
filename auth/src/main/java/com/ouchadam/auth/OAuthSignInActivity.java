package com.ouchadam.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OAuthSignInActivity extends Activity {

    public static final String BASE_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=";
    public static final String REDIRECT_URI = "http://com.ouchadam.kanto";
    public static final String CLIENT_ID = "6224o4_ylYWflQ";

    private AccountAuthenticatorResponse authenticatorResponse;

    public static Intent create(Context context, AccountAuthenticatorResponse response) {
        String responseType = "code";
        String requestId = "RANDOM_STRING";
        String duration = "permanent";
        String scope = "read,identity,mysubreddits";

        Intent intent = new Intent(context, OAuthSignInActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.setData(
                Uri.parse(
                        BASE_URL
                                + CLIENT_ID
                                + "&response_type=" + responseType
                                + "&state=" + requestId
                                + "&redirect_uri=" + REDIRECT_URI
                                + "&duration=" + duration
                                + "&scope=" + scope));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_web);

        authenticatorResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        authenticatorResponse.onRequestContinued();

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
        TokenAcquirer.newInstance(OAuthSignInActivity.this).requestNewToken(redirectUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccessToken2>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AccessToken2 accessToken) {
                        Account account = new Account(accessToken.getAccoutName(), "accountType");

                        AccountManager accountManager = AccountManager.get(OAuthSignInActivity.this);

                        Bundle userdata = createUserData(accessToken);
                        accountManager.addAccountExplicitly(account, accessToken.getAccessToken(), userdata);
                        accountManager.setAuthToken(account, account.type, account.type);

                        finishLogin(accessToken, account);
                    }
                });
    }

    private Bundle createUserData(AccessToken2 accessToken) {
        Bundle bundle = new Bundle();

        bundle.putLong(Authenticator.KEY_TOKEN_EXPIRY, accessToken.getExpiryTime());

        return bundle;
    }

    private void finishLogin(Token token, Account account) {
        Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, token.getAccessToken());

        forwardAccountAuthenticatorResult();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void forwardAccountAuthenticatorResult() {
        Bundle result = getIntent().getExtras();
        if (result == null) {
            authenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED, "cancelled");
        } else {
            authenticatorResponse.onResult(result);
        }
    }

}
