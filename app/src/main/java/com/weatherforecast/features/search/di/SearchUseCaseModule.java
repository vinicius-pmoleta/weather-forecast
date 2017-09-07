package com.weatherforecast.features.search.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.search.usecase.FetchLocationsSearchedUseCase;
import com.weatherforecast.features.search.usecase.FetchWeatherUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchUseCaseModule {

    @ActivityScope
    @Provides
    public FetchWeatherUseCase provideFetchWeatherUseCase(@NonNull final ForecastRepository forecastRepository,
                                                          @NonNull final ExecutionConfiguration configuration,
                                                          @NonNull final WeatherForecastDatabase database) {
        return new FetchWeatherUseCase(forecastRepository, configuration, database);
    }

    @ActivityScope
    @Provides
    public FetchLocationsSearchedUseCase provideLocationsSearchedUseCase(@NonNull final ExecutionConfiguration configuration,
                                                                         @NonNull final WeatherForecastDatabase database) {
        return new FetchLocationsSearchedUseCase(configuration, database);
    }

}
