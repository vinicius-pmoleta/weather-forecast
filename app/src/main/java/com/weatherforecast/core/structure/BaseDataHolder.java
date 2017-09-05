package com.weatherforecast.core.structure;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

public class BaseDataHolder extends ViewModel {

    private List<Subscription> subscriptions;

    public void addSubscription(@NonNull final Subscription subscription) {
        if (subscriptions == null) {
            subscriptions = new ArrayList<>();
        }
        subscriptions.add(subscription);
    }

    @Override
    protected void onCleared() {
        for (final Subscription subscription : subscriptions) {
            subscription.cancel();
        }
        super.onCleared();
    }
}
