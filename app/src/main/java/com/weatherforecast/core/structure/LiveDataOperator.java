package com.weatherforecast.core.structure;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LiveDataOperator {

    public static <T> boolean isContentAvailable(@Nullable final LiveData<T> data) {
        return data != null && data.getValue() != null;
    }

    public static <T> void removeDataObservers(@Nullable final LiveData<T> data,
                                               @NonNull final LifecycleOwner owner) {
        if (data != null && data.hasObservers()) {
            data.removeObservers(owner);
        }
    }
}
