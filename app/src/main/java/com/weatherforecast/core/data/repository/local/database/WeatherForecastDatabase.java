package com.weatherforecast.core.data.repository.local.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.weatherforecast.features.common.data.entity.CityEntity;
import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.repository.CityDao;
import com.weatherforecast.features.common.data.repository.ForecastDao;

@Database(
        entities = {CityEntity.class, ForecastEntity.class},
        version = 1
)
public abstract class WeatherForecastDatabase extends RoomDatabase {

    public abstract CityDao cityDao();

    public abstract ForecastDao forecastDao();

}
