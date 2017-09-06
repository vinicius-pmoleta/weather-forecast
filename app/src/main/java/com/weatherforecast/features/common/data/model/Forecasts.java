package com.weatherforecast.features.common.data.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class Forecasts {

    @SerializedName("list")
    public abstract List<Forecast> forecasts();

    public abstract City city();

    public static Forecasts create(@NonNull final List<Forecast> forecasts, @NonNull final City city) {
        return new AutoValue_Forecasts(forecasts, city);
    }

    public static TypeAdapter<Forecasts> typeAdapter(Gson gson) {
        return new AutoValue_Forecasts.GsonTypeAdapter(gson);
    }

}
