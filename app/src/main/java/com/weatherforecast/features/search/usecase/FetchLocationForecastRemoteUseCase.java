package com.weatherforecast.features.search.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.data.usecase.LiveUseCase;
import com.weatherforecast.features.common.data.model.Forecasts;

import io.reactivex.Flowable;

public class FetchLocationForecastRemoteUseCase extends LiveUseCase<Forecasts, String> {

    private final ForecastRepository repository;

    public FetchLocationForecastRemoteUseCase(@NonNull final ForecastRepository repository, @NonNull final ExecutionConfiguration configuration) {
        super(configuration);
        this.repository = repository;
    }

    @Override
    public Flowable<Forecasts> buildUseCaseObservable(@Nullable final String location) {
        return repository.getForecast(location);
    }
}
