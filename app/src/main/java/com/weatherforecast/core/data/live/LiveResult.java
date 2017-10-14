package com.weatherforecast.core.data.live;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LiveResult<T> {

    private final LiveData<T> data;
    private final LiveData<Throwable> error;

    public LiveResult(@Nullable final LiveData<T> data, @Nullable final LiveData<Throwable> error) {
        this.data = data;
        this.error = error;
    }

    public LiveData<T> data() {
        return data;
    }

    public LiveData<Throwable> error() {
        return error;
    }

    public void observe(@NonNull final LifecycleOwner owner,
                        @Nullable final Observer<T> dataObserver,
                        @Nullable final Observer<Throwable> errorObserver) {
        if (data != null && dataObserver != null) {
            data.observe(owner, dataObserver);
        }
        if (error != null && errorObserver != null) {
            error.observe(owner, errorObserver);
        }
    }

}
