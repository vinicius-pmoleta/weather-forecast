package com.weatherforecast.features.search.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.search.data.Weather;

import io.reactivex.Flowable;

public class FetchWeatherUseCase extends UseCase<Weather, String> {

    private final ForecastRepository repository;

    public FetchWeatherUseCase(@NonNull final ForecastRepository repository,
                               @NonNull final ExecutionConfiguration configuration) {
        super(configuration);
        this.repository = repository;
    }

    @Override
    public Flowable<Weather> buildUseCaseObservable(@Nullable final String location) {
        return repository.getWeather(location);
    }

}
