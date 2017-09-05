package com.weatherforecast.core.di.component;

import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.di.module.ApplicationModule;
import com.weatherforecast.core.di.module.ConfigurationModule;
import com.weatherforecast.core.di.module.DatabaseModule;
import com.weatherforecast.core.di.module.NetworkModule;
import com.weatherforecast.core.di.module.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
        RepositoryModule.class,
        ConfigurationModule.class,
        DatabaseModule.class
})
public interface ApplicationComponent {

    WeatherForecastApplication application();

    ForecastRepository forecastRepository();

    ExecutionConfiguration executeConfiguration();

    WeatherForecastDatabase database();

}
