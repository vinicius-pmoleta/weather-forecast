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

public abstract class UseCase<T, Params> {

    private final ExecutionConfiguration configuration;

    public UseCase(@NonNull final ExecutionConfiguration configuration) {
        this.configuration = configuration;
    }

    public abstract Flowable<T> buildUseCaseObservable(@Nullable final Params params);

    public Flowable<T> execute(@Nullable final Params params,
                               @NonNull final Consumer<Subscription> onSubscribe,
                               @NonNull final Consumer<Throwable> onError,
                               @NonNull final Action onComplete) {
        return execute(params, onSubscribe, onError, onComplete, new DefaultTransformer(configuration));
    }


    private Flowable<T> execute(@Nullable final Params params,
                                @NonNull final Consumer<Subscription> onSubscribe,
                                @NonNull final Consumer<Throwable> onError,
                                @NonNull final Action onComplete,
                                @NonNull final FlowableTransformer<T, T> transformer) {
        return buildUseCaseObservable(params)
                .compose(transformer)
                .doOnSubscribe(onSubscribe)
                .doOnError(onError)
                .doOnComplete(onComplete);
    }

    public LiveData<T> executeLive(@Nullable final Params params,
                                   @NonNull final Consumer<Subscription> onSubscribe,
                                   @NonNull final Consumer<Throwable> onError,
                                   @NonNull final Action onComplete) {
        return executeLive(params, onSubscribe, onError, onComplete, new DefaultTransformer(configuration));
    }

    private LiveData<T> executeLive(@Nullable final Params params,
                                    @NonNull final Consumer<Subscription> onSubscribe,
                                    @NonNull final Consumer<Throwable> onError,
                                    @NonNull final Action onComplete,
                                    @NonNull final FlowableTransformer<T, T> transformer) {
        final Flowable<T> data = execute(params, onSubscribe, onError, onComplete, transformer);
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


    @SuppressWarnings("unused")
    public static class DefaultOnSubscribe implements Consumer<Subscription> {

        @Override
        public void accept(@io.reactivex.annotations.NonNull Subscription subscription) throws Exception {
        }
    }

    @SuppressWarnings("unused")
    public static class DefaultOnError implements Consumer<Throwable> {

        @Override
        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
            throw new RuntimeException("Not implemented", throwable);
        }
    }

    public static class DefaultOnComplete implements Action {

        @Override
        public void run() throws Exception {
        }
    }

}
