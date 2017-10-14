package com.weatherforecast.core.data.live;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

public class LiveDataOperator {

    public static <T> boolean isDataAvailable(@Nullable final LiveResult<T> result) {
        return result != null && result.data() != null && result.data().getValue() != null;
    }

    public static <T> void removeResultObservers(@Nullable final LiveResult<T> result,
                                                 @NonNull final LifecycleOwner owner) {
        if (result != null) {
            removeDataObservers(result.data(), owner);
            removeDataObservers(result.error(), owner);
        }
    }

    @VisibleForTesting
    static <T> void removeDataObservers(@Nullable final LiveData<T> data,
                                        @NonNull final LifecycleOwner owner) {
        if (data != null && data.hasObservers()) {
            data.removeObservers(owner);
        }
    }
}
