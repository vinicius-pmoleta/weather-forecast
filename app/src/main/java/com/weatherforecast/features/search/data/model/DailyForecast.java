package com.weatherforecast.features.search.data.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.weatherforecast.features.common.data.model.Forecast;

import java.util.List;

@AutoValue
public abstract class DailyForecast {

    public abstract String date();

    public abstract List<Forecast> forecasts();

    public static DailyForecast create(@NonNull final String date, @NonNull final List<Forecast> forecasts) {
        return new AutoValue_DailyForecast(date, forecasts);
    }

}
