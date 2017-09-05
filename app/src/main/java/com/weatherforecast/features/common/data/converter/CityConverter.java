package com.weatherforecast.features.common.data.converter;


import android.support.annotation.NonNull;

import com.weatherforecast.features.common.data.entity.CityEntity;
import com.weatherforecast.features.common.data.model.City;

public class CityConverter {

    public static CityEntity toEntity(@NonNull final City model) {
        final CityEntity entity = new CityEntity();
        entity.id = model.id();
        entity.name = model.name();
        return entity;
    }

}
