package com.weatherforecast.features.search.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.common.data.model.Forecasts;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

public class FetchRemoteForecastUseCase extends UseCase<Forecasts, String> {

    private final ForecastRepository repository;
    private final WeatherForecastDatabase database;

    public FetchRemoteForecastUseCase(@NonNull final ForecastRepository repository,
                                      @NonNull final ExecutionConfiguration configuration,
                                      @NonNull final WeatherForecastDatabase database) {
        super(configuration);
        this.repository = repository;
        this.database = database;
    }

    @Override
    public Flowable<Forecasts> buildUseCaseObservable(@Nullable final String location) {
        return repository.getForecast(location).delay(10, TimeUnit.SECONDS);
    }
}
