package com.ouchadam.loldr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ouchadam.auth.Foo;
import com.ouchadam.ui.MainActivityPresenter;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private MainActivityPresenter mainActivityPresenter;
    private Foo foo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityPresenter = MainActivityPresenter.onCreate(this);

        UUID deviceId = UUID.randomUUID();

        foo = new Foo(deviceId);
        foo.requestSignedOutToken(new Foo.Callback() {
            @Override
            public void onSuccess(Foo.Token token) {
                Toast.makeText(MainActivity.this, token.getUrlResponse(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
