package com.weatherforecast.features.search.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.search.usecase.FetchLocalForecastUseCase;
import com.weatherforecast.features.search.usecase.FetchRemoteForecastUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class ForecastSearchUseCaseModule {

    @ActivityScope
    @Provides
    public FetchRemoteForecastUseCase provideFetchRemoteForecastUseCase(@NonNull final ForecastRepository forecastRepository,
                                                                        @NonNull final ExecutionConfiguration configuration,
                                                                        @NonNull final WeatherForecastDatabase database) {
        return new FetchRemoteForecastUseCase(forecastRepository, configuration, database);
    }

    @ActivityScope
    @Provides
    public FetchLocalForecastUseCase provideFetchLocalForecastUseCase(@NonNull final ExecutionConfiguration configuration,
                                                                      @NonNull final WeatherForecastDatabase database) {
        return new FetchLocalForecastUseCase(configuration, database);
    }

}
