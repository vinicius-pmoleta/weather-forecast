package com.weatherforecast.features.common.data.model;

import android.support.annotation.NonNull;

public class City {

    private final long id;
    private final String name;

    public City(final long id, @NonNull final String name) {
        this.id = id;
        this.name = name;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }
}
