package com.weatherforecast.features.search.data.model;

import android.support.annotation.NonNull;

import com.weatherforecast.features.common.data.model.Forecast;

import java.util.List;

public class DailyForecast {

    private final String date;
    private final List<Forecast> forecasts;

    public DailyForecast(@NonNull final String date, @NonNull final List<Forecast> forecasts) {
        this.date = date;
        this.forecasts = forecasts;
    }

    public String date() {
        return date;
    }

    public List<Forecast> forecasts() {
        return forecasts;
    }
}
