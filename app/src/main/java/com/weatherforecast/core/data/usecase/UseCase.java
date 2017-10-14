package com.weatherforecast.core.data.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.weatherforecast.core.data.live.LiveDataReactiveConverter;
import com.weatherforecast.core.data.live.LiveResult;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public abstract class UseCase<T, Params> {

    private static final String TAG = UseCase.class.getSimpleName();

    private final ExecutionConfiguration configuration;

    public UseCase(@NonNull final ExecutionConfiguration configuration) {
        this.configuration = configuration;
    }

    public abstract Flowable<T> buildUseCaseObservable(@Nullable final Params params);

    public Flowable<T> execute(@Nullable final Params params,
                               @NonNull final Consumer<Subscription> onSubscribe) {
        return execute(params, onSubscribe, new DefaultTransformer(configuration));
    }


    private Flowable<T> execute(@Nullable final Params params,
                                @NonNull final Consumer<Subscription> onSubscribe,
                                @NonNull final FlowableTransformer<T, T> transformer) {
        return buildUseCaseObservable(params)
                .compose(transformer)
                .doOnSubscribe(onSubscribe)
                .doOnError(new DefaultOnError());
    }

    public LiveResult<T> executeLive(@Nullable final Params params,
                                     @NonNull final Consumer<Subscription> onSubscribe,
                                     @NonNull final Action onComplete) {
        return executeLive(params, onSubscribe, onComplete, new DefaultTransformer(configuration));
    }

    private LiveResult<T> executeLive(@Nullable final Params params,
                                      @NonNull final Consumer<Subscription> onSubscribe,
                                      @NonNull final Action onComplete,
                                      @NonNull final FlowableTransformer<T, T> transformer) {
        final Flowable<T> data = execute(params, onSubscribe, transformer);
        return LiveDataReactiveConverter.fromPublisher(data, onComplete);
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
    public static class DefaultOnError implements Consumer<Throwable> {

        @Override
        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
            Log.e(TAG, "Error during execution", throwable);
            throw new RuntimeException("Not implemented", throwable);
        }
    }

    @SuppressWarnings("unused")
    public static class DefaultOnComplete implements Action {

        @Override
        public void run() throws Exception {
        }
    }

}
