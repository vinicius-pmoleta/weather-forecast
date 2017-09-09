package com.weatherforecast.features.dailyforecast.presentation.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class DailyForecastScreenModel {

    public abstract String date();

    public abstract List<ForecastScreenModel> forecasts();

    public static DailyForecastScreenModel create(@NonNull final String date,
                                                  @NonNull final List<ForecastScreenModel> forecasts) {
        return new AutoValue_DailyForecastScreenModel(date, forecasts);
    }

}
