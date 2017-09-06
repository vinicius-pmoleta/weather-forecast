package com.weatherforecast.features.dailyforecast.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.dailyforecast.usecase.FetchLocalForecastUseCase;
import com.weatherforecast.features.dailyforecast.usecase.UpdateLocalForecastUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class DailyForecastUseCaseModule {

    @ActivityScope
    @Provides
    public UpdateLocalForecastUseCase provideUpdateLocalForecastUseCase(@NonNull final ForecastRepository forecastRepository,
                                                                        @NonNull final ExecutionConfiguration configuration,
                                                                        @NonNull final WeatherForecastDatabase database) {
        return new UpdateLocalForecastUseCase(forecastRepository, configuration, database);
    }

    @ActivityScope
    @Provides
    public FetchLocalForecastUseCase provideFetchLocalForecastUseCase(@NonNull final ExecutionConfiguration configuration,
                                                                      @NonNull final WeatherForecastDatabase database) {
        return new FetchLocalForecastUseCase(configuration, database);
    }

}
