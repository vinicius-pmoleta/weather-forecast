package com.weatherforecast.features.search.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.common.data.converter.CityConverter;
import com.weatherforecast.features.common.data.repository.CityDao;
import com.weatherforecast.features.search.data.Weather;

import io.reactivex.Flowable;

public class FetchWeatherUseCase extends UseCase<Weather, String> {

    private final ForecastRepository repository;
    private final CityDao cityDao;

    public FetchWeatherUseCase(@NonNull final ForecastRepository repository,
                               @NonNull final ExecutionConfiguration configuration,
                               @NonNull final WeatherForecastDatabase database) {
        super(configuration);
        this.repository = repository;
        this.cityDao = database.cityDao();
    }

    @Override
    public Flowable<Weather> buildUseCaseObservable(@Nullable final String location) {
        return repository.getWeather(location)
                .doOnNext(weather -> cityDao.insert(CityConverter.toEntity(weather)));
    }

}
