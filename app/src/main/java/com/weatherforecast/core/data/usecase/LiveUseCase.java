package com.weatherforecast.core.data.usecase;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public abstract class LiveUseCase<T, Params> {

    private final ExecutionConfiguration configuration;

    public LiveUseCase(@NonNull final ExecutionConfiguration configuration) {
        this.configuration = configuration;
    }

    public abstract Flowable<T> buildUseCaseObservable(@Nullable final Params params);

    public LiveData<T> execute(@Nullable final Params params,
                               @NonNull final Consumer<Subscription> onSubscribe,
                               @NonNull final Consumer<Throwable> onError,
                               @NonNull final Action onComplete) {
        return execute(params, onSubscribe, onError, onComplete, new DefaultTransformer(configuration));
    }

    public LiveData<T> execute(@Nullable final Params params,
                               @NonNull final Consumer<Subscription> onSubscribe,
                               @NonNull final Consumer<Throwable> onError,
                               @NonNull final Action onComplete,
                               @NonNull final FlowableTransformer<T, T> transformer) {
        final Flowable<T> data = buildUseCaseObservable(params)
                .compose(transformer)
                .doOnSubscribe(onSubscribe)
                .doOnError(onError)
                .doOnComplete(onComplete);

        return LiveDataReactiveStreams.fromPublisher(data);
    }

    private class DefaultTransformer implements FlowableTransformer<T, T> {

        private final ExecutionConfiguration configuration;

        DefaultTransformer(@NonNull final ExecutionConfiguration configuration) {
            this.configuration = configuration;
        }

        @Override
        public Publisher<T> apply(@io.reactivex.annotations.NonNull Flowable<T> upstream) {
            return upstream
                    .subscribeOn(configuration.execution())
                    .observeOn(configuration.postExecution());
        }
    }

    public class DefaulOnSubscribe implements Consumer<Subscription> {

        @Override
        public void accept(@io.reactivex.annotations.NonNull Subscription subscription) throws Exception {
        }
    }

    private class DefaultOnError implements Consumer<Throwable> {

        @Override
        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
            throw new RuntimeException("Not implemented", throwable);
        }
    }

    private class DefaultOnComplete implements Action {

        @Override
        public void run() throws Exception {
        }
    }

}
