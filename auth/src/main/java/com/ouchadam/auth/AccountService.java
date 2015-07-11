package com.ouchadam.auth;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountService extends Service {

    private Authenticator all4AccountAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        this.all4AccountAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (AccountManager.ACTION_AUTHENTICATOR_INTENT.equals(intent.getAction())) {
            return all4AccountAuthenticator.getIBinder();
        }
        return null;
    }

}
