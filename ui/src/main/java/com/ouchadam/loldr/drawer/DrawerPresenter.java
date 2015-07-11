package com.ouchadam.loldr.drawer;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.SourceProvider;
import com.ouchadam.loldr.Ui;

public class DrawerPresenter<T extends DataSource<Ui.Subscription>> {

    private final NavigationView navigationView;
    private final Listener listener;

    public DrawerPresenter(NavigationView navigationView, Listener listener) {
        this.navigationView = navigationView;
        this.listener = listener;
    }

    public void present(T source) {
        for (int index = 0; index < source.size(); index++) {

            Ui.Subscription subscription = source.get(index);

            Intent intent = new Intent();

            intent.putExtra("id", subscription.getId());
            intent.putExtra("name", subscription.getName());

            navigationView.getMenu().add(subscription.getName()).setIntent(intent);
        }
        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                final Intent intent = menuItem.getIntent();

                listener.onSubscriptionClicked(new Ui.Subscription() {
                    @Override
                    public String getId() {
                        return intent.getStringExtra("id");
                    }

                    @Override
                    public String getName() {
                        return intent.getStringExtra("name");
                    }
                });

                return true;
            }
        });
    }

    public interface Listener {
        void onSubscriptionClicked(Ui.Subscription subscription);
    }

    interface SubscriptionSourceProvider<T extends DataSource<Ui.Subscription>> extends SourceProvider<Ui.Subscription, T> {
    }

}
