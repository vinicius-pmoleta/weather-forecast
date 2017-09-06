package com.weatherforecast.features.common.data.helper;


import android.support.annotation.NonNull;

import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.search.data.model.DailyForecast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestDataCreator {

    @NonNull
    public static ForecastEntity createEntityWithDateAndCityId(@NonNull final String date,
                                                               final long cityId) {
        final ForecastEntity entity = new ForecastEntity();
        entity.date = date;
        entity.cityId = cityId;
        entity.currentTemperature = 20.0;
        entity.minimumTemperature = 10.0;
        entity.maximumTemperature = 30.0;
        entity.condition = "Condition";
        entity.icon = "condition-00";
        return entity;
    }

    @NonNull
    public static DailyForecast createDailyForecastWithDateAndTimes(@NonNull final String date,
                                                                    @NonNull final List<String> times) {
        final List<Forecast> forecasts = new ArrayList<>();
        for (final String time : times) {
            final Forecast forecast = createForecastModelWithDataAndTime(date, time);
            forecasts.add(forecast);
        }
        return DailyForecast.create(date, forecasts);
    }

    @NonNull
    public static Forecast createForecastModelWithDataAndTime(@NonNull final String date,
                                                              @NonNull final String time) {
        final Forecast.Temperature temperature = Forecast.Temperature.create(20.0, 10.0, 30.0);
        final Forecast.Condition condition = Forecast.Condition.create("Condition", "condition-00");
        return Forecast.create(date + " " + time, temperature, Collections.singletonList(condition));
    }

}
