package com.weatherforecast.core.data.repository.local.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.weatherforecast.features.common.data.entity.City;
import com.weatherforecast.features.common.data.repository.CityDao;

@Database(
        entities = {City.class},
        version = 1
)
public abstract class WeatherForecastDatabase extends RoomDatabase {

    public abstract CityDao cityDao();

}
