package com.ouchadam.loldr.feed;

import android.os.Bundle;

import com.ouchadam.loldr.BaseActivity;

public class FeedActivity extends BaseActivity {

    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = Presenter.onCreate(this);
    }

}
