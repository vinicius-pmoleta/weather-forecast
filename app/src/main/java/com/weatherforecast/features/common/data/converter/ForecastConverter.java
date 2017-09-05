package com.weatherforecast.features.common.data.converter;

import android.support.annotation.NonNull;

import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.common.data.model.Forecast.Condition;
import com.weatherforecast.features.common.data.model.Forecast.Temperature;

import java.util.Collections;

public class ForecastConverter {

    public static Forecast fromEntity(@NonNull final ForecastEntity entity) {
        final Temperature temperature = new Temperature(entity.currentTemperature, entity.minimumTemperature, entity.maximumTemperature);
        final Condition condition = new Condition(entity.condition, entity.icon);
        return new Forecast(entity.date, temperature, Collections.singletonList(condition));
    }

    public static ForecastEntity toEntity(@NonNull final Forecast model, @NonNull final City city) {
        final ForecastEntity entity = new ForecastEntity();

        entity.cityId = city.id();
        entity.date = model.date();
        entity.currentTemperature = model.temperature().current();
        entity.minimumTemperature = model.temperature().minimum();
        entity.maximumTemperature = model.temperature().maximum();

        if (model.conditions() != null && !model.conditions().isEmpty()) {
            final Condition condition = model.conditions().get(0);
            entity.condition = condition.name();
            entity.icon = condition.icon();
        }

        return entity;
    }
}
