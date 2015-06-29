package com.ouchadam.ui;

import android.app.Activity;
import android.widget.Toast;

public class MainActivityPresenter {

    public static MainActivityPresenter onCreate(Activity activity) {
        activity.setContentView(R.layout.activity_main);

        Toast.makeText(activity, "Hello world", Toast.LENGTH_SHORT).show();

        return new MainActivityPresenter();
    }
}
