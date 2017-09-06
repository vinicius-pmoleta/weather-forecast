package com.weatherforecast.features.common.data.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class City {

    public abstract long id();

    public abstract String name();

    public static City create(final long id, @NonNull final String name) {
        return new AutoValue_City(id, name);
    }

    public static TypeAdapter<City> typeAdapter(Gson gson) {
        return new AutoValue_City.GsonTypeAdapter(gson);
    }

}
