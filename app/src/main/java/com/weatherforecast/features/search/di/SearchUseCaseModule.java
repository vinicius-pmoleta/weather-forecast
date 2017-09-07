package com.weatherforecast.features.search.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.search.usecase.FetchWeatherUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchUseCaseModule {

    @ActivityScope
    @Provides
    public FetchWeatherUseCase provideFetchWeatherUseCase(@NonNull final ForecastRepository forecastRepository,
                                                          @NonNull final ExecutionConfiguration configuration) {
        return new FetchWeatherUseCase(forecastRepository, configuration);
    }

}
