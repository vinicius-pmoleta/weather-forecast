package com.weatherforecast.core.di.module;

import android.support.annotation.NonNull;

import com.weatherforecast.core.WeatherForecastApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final WeatherForecastApplication application;

    public ApplicationModule(@NonNull final WeatherForecastApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public WeatherForecastApplication providesApplication() {
        return application;
    }

}
