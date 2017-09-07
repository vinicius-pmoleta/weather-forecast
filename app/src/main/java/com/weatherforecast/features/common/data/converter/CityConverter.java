package com.weatherforecast.features.common.data.converter;


import android.support.annotation.NonNull;

import com.weatherforecast.features.common.data.entity.CityEntity;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.search.data.Weather;

public class CityConverter {

    public static CityEntity toEntity(@NonNull final City model) {
        final CityEntity entity = new CityEntity();
        entity.id = model.id();
        entity.name = model.name();
        return entity;
    }

    public static CityEntity toEntity(@NonNull final Weather model) {
        final CityEntity entity = new CityEntity();
        entity.id = model.id();
        entity.name = model.name();
        return entity;
    }

    public static City fromEntity(@NonNull final CityEntity entity) {
        return City.create(entity.id, entity.name);
    }
}
