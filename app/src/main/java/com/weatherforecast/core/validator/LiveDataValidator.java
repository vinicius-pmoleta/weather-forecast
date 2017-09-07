package com.weatherforecast.core.validator;

import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

public class LiveDataValidator {

    public static <T> boolean isContentAvailable(@Nullable final LiveData<T> data) {
        return data != null && data.getValue() != null;
    }
}
