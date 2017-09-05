package com.weatherforecast.features.common.data.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecasts {

    @SerializedName("list")
    private final List<Forecast> forecasts;
    private final City city;

    public Forecasts(@NonNull final List<Forecast> forecasts, @NonNull final City city) {
        this.forecasts = forecasts;
        this.city = city;
    }

    public List<Forecast> forecasts() {
        return forecasts;
    }

    public City city() {
        return city;
    }
}
