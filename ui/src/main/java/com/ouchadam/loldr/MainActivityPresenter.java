package com.ouchadam.loldr;

import android.app.Activity;
import android.widget.Toast;

import ouchadam.com.loldr.R;

class MainActivityPresenter {

    public static MainActivityPresenter onCreate(Activity activity) {
        activity.setContentView(R.layout.activity_main);

        Toast.makeText(activity, "Hello world", Toast.LENGTH_SHORT).show();

        return new MainActivityPresenter();
    }
}
