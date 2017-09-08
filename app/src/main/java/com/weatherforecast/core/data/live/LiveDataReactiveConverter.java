package com.weatherforecast.core.data.live;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Code adapted from android.arch.lifecycle.LiveDataReactiveStreams
 */
public class LiveDataReactiveConverter {

    public static <T> LiveData<T> fromPublisher(@NonNull final Publisher<T> publisher,
                                                @NonNull final Consumer<Subscription> doOnSubscribe,
                                                @NonNull final Consumer<Throwable> doOnError,
                                                @NonNull final Action doOnComplete) {
        MutableLiveData<T> liveData = new MutableLiveData<>();
        final WeakReference<MutableLiveData<T>> liveDataRef = new WeakReference<>(liveData);

        publisher.subscribe(new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
                try {
                    doOnSubscribe.accept(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
                try {
                    doOnError.accept(error);
                } catch (Exception e) {
                    throw new RuntimeException(e);
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

        return liveData;
    }

}
