package com.ouchadam.loldr.drawer;

import android.support.design.widget.NavigationView;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;

public class DrawerPresenter<T extends DataSource<Ui.Subscription>> {

    private final NavigationView navigationView;

    public DrawerPresenter(NavigationView navigationView) {
        this.navigationView = navigationView;
    }

    public void present(T source) {
        for (int index = 0; index < source.size(); index++) {
            navigationView.getMenu().add(source.get(index).getName());
        }
    }

    interface SubscriptionSourceProvider<T extends DataSource<Ui.Subscription>> extends SourceProvider<Ui.Subscription, T> {
    }

}
