package com.ouchadam.loldr.drawer;

import com.ouchadam.loldr.DataSource;
import com.ouchadam.loldr.Ui;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionProvider implements DrawerPresenter.SubscriptionSourceProvider<SubscriptionProvider.SubscriptionSource> {

    private final SubscriptionSource subscriptionSource = new SubscriptionSource();

    @Override
    public void swap(SubscriptionSource source) {
        subscriptionSource.subscriptions.addAll(source.subscriptions);
    }

    @Override
    public Ui.Subscription get(int position) {
        return subscriptionSource.get(position);
    }

    @Override
    public int size() {
        return subscriptionSource.size();
    }

    public static class SubscriptionSource implements DataSource<Ui.Subscription> {

        private final List<Ui.Subscription> subscriptions;

        public SubscriptionSource() {
            this(new ArrayList<Ui.Subscription>());
        }

        public SubscriptionSource(List<Ui.Subscription> subscriptions) {
            this.subscriptions = subscriptions;
        }

        @Override
        public int size() {
            return subscriptions.size();
        }

        @Override
        public Ui.Subscription get(final int position) {
            return subscriptions.get(position);
        }

    }
}
