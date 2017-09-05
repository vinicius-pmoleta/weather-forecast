package com.weatherforecast.features.search.di;

import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.search.usecase.FetchLocationForecastUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class ForecastSearchUseCaseModule {

    @ActivityScope
    @Provides
    public FetchLocationForecastUseCase provideFetchLocationForecastUseCase() {
        return new FetchLocationForecastUseCase();
    }

}
