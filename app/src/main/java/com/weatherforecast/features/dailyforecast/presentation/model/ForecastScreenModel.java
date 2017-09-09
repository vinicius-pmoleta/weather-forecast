package com.weatherforecast.features.dailyforecast.presentation.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ForecastScreenModel {

    public abstract String time();

    public abstract String conditionIcon();

    public abstract String temperature();

    public static ForecastScreenModel create(@NonNull final String time,
                                             @NonNull final String conditionIcon,
                                             @NonNull final String temperature) {
        return new AutoValue_ForecastScreenModel(time, conditionIcon, temperature);
    }

}
