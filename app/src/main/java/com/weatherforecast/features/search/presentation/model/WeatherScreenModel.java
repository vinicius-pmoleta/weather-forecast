package com.weatherforecast.features.search.presentation.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class WeatherScreenModel {

    public abstract long id();

    public abstract String location();

    public abstract String condition();

    public abstract String conditionIcon();

    public abstract String temperatureMaximum();

    public abstract String temperatureCurrent();

    public abstract String temperatureMinimum();

    public static WeatherScreenModel create(final long id,
                                            @NonNull final String location,
                                            @NonNull final String condition,
                                            @NonNull final String conditionIcon,
                                            @NonNull final String temperatureMaximum,
                                            @NonNull final String temperatureCurrent,
                                            @NonNull final String temperatureMinimum) {
        return new AutoValue_WeatherScreenModel(id, location, condition, conditionIcon,
                temperatureMaximum, temperatureCurrent, temperatureMinimum);
    }

}
