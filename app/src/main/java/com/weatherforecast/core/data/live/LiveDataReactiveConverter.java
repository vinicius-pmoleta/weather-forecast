package com.weatherforecast.core.data.live;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Action;

/**
 * Code adapted from android.arch.lifecycle.LiveDataReactiveStreams
 */
public class LiveDataReactiveConverter {

    public static <T> LiveResult<T> fromPublisher(@NonNull final Publisher<T> publisher,
                                                  @NonNull final Action doOnComplete) {
        final MutableLiveData<T> liveData = new MutableLiveData<>();
        final WeakReference<MutableLiveData<T>> liveDataRef = new WeakReference<>(liveData);

        final MutableLiveData<Throwable> liveError = new MutableLiveData<>();
        final WeakReference<MutableLiveData<Throwable>> liveErrorRef = new WeakReference<>(liveError);

        final LiveResult<T> liveResult = new LiveResult<>(liveData, liveError);
        publisher.subscribe(new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(final T t) {
                final MutableLiveData<T> liveData = liveDataRef.get();
                if (liveData != null) {
                    liveData.postValue(t);
                }
            }

            @Override
            public void onError(Throwable error) {
                final MutableLiveData<Throwable> liveError = liveErrorRef.get();
                if (liveError != null) {
                    liveError.postValue(error);
                }
            }

            @Override
            public void onComplete() {
                try {
                    doOnComplete.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return liveResult;
    }

}
