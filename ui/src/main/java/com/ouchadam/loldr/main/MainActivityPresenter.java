package com.ouchadam.loldr.main;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.ouchadam.loldr.ui.R;

public class MainActivityPresenter {

    public static MainActivityPresenter onCreate(Activity activity, final Listener listener) {
        activity.setContentView(R.layout.activity_main);

        activity.findViewById(R.id.button_anon_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickAnonToken();
            }
        });

        activity.findViewById(R.id.button_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickUserToken();
            }
        });

        Toast.makeText(activity, "Hello world", Toast.LENGTH_SHORT).show();

        return new MainActivityPresenter();
    }

    public interface Listener {
        void onClickUserToken();
        void onClickAnonToken();
    }
}
