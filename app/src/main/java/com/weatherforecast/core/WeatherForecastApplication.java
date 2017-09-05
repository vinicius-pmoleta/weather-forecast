package com.weatherforecast.core;

import android.app.Application;

import com.weatherforecast.core.di.component.ApplicationComponent;
import com.weatherforecast.core.di.component.DaggerApplicationComponent;
import com.weatherforecast.core.di.module.ApplicationModule;
import com.weatherforecast.core.di.module.ConfigurationModule;
import com.weatherforecast.core.di.module.NetworkModule;
import com.weatherforecast.core.di.module.RepositoryModule;

public class WeatherForecastApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initialiseDependencyInjection();
    }

    public ApplicationComponent applicationComponent() {
        return applicationComponent;
    }

    private void initialiseDependencyInjection() {
        DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule())
                .repositoryModule(new RepositoryModule())
                .configurationModule(new ConfigurationModule())
                .build();
    }
}
