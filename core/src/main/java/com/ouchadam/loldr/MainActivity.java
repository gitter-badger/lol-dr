package com.ouchadam.loldr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ouchadam.auth.Foo;
import com.ouchadam.ui.MainActivityPresenter;

public class MainActivity extends BaseActivity {

    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityPresenter = MainActivityPresenter.onCreate(this);

        new Foo().requestAuthentication(this);
    }

}
