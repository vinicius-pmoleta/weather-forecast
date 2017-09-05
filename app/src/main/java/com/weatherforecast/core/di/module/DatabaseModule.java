package com.weatherforecast.core.di.module;

import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public WeatherForecastDatabase provideDatabase(@NonNull final WeatherForecastApplication application) {
        return Room.databaseBuilder(application.getApplicationContext(),
                WeatherForecastDatabase.class, "weather-forecast-database").build();
    }

}
