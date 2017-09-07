package com.weatherforecast.features.search.data;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.weatherforecast.features.common.data.model.Forecast;

import java.util.List;

@AutoValue
public abstract class Weather {

    public abstract long id();

    public abstract String name();

    @SerializedName("main")
    public abstract Forecast.Temperature temperature();

    @SerializedName("weather")
    public abstract List<Forecast.Condition> conditions();

    public static Weather create(final long id, @NonNull final String name,
                                 @NonNull final Forecast.Temperature temperature,
                                 @NonNull final List<Forecast.Condition> conditions) {
        return new AutoValue_Weather(id, name, temperature, conditions);
    }

    public static TypeAdapter<Weather> typeAdapter(Gson gson) {
        return new AutoValue_Weather.GsonTypeAdapter(gson);
    }

}
