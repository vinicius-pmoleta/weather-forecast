package com.weatherforecast.features.common.data.converter;

import android.support.annotation.NonNull;

import com.weatherforecast.features.common.data.entity.ForecastEntity;
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

}
