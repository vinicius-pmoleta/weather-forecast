package com.weatherforecast.features.search.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.data.usecase.LiveUseCase;
import com.weatherforecast.features.common.data.model.Forecasts;

import io.reactivex.Flowable;

public class FetchLocationForecastRemoteUseCase extends LiveUseCase<Forecasts, String> {

    private final ForecastRepository repository;
    private final WeatherForecastDatabase database;

    public FetchLocationForecastRemoteUseCase(@NonNull final ForecastRepository repository,
                                              @NonNull final ExecutionConfiguration configuration,
                                              @NonNull final WeatherForecastDatabase database) {
        super(configuration);
        this.repository = repository;
        this.database = database;
    }

    @Override
    public Flowable<Forecasts> buildUseCaseObservable(@Nullable final String location) {
        return repository.getForecast(location);
    }
}
