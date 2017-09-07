package com.weatherforecast.features.dailyforecast.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.common.data.converter.CityConverter;
import com.weatherforecast.features.common.data.converter.ForecastConverter;
import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.repository.CityDao;
import com.weatherforecast.features.common.data.repository.ForecastDao;

import java.util.List;

import io.reactivex.Flowable;

public class UpdateLocalForecastUseCase extends UseCase<List<ForecastEntity>, Long> {

    private final ForecastRepository repository;
    private final CityDao cityDao;
    private final ForecastDao forecastDao;

    public UpdateLocalForecastUseCase(@NonNull final ForecastRepository repository,
                                      @NonNull final ExecutionConfiguration configuration,
                                      @NonNull final WeatherForecastDatabase database) {
        super(configuration);
        this.repository = repository;
        this.cityDao = database.cityDao();
        this.forecastDao = database.forecastDao();
    }

    @Override
    public Flowable<List<ForecastEntity>> buildUseCaseObservable(@Nullable final Long id) {
        return repository.getForecast(id)
                .doOnNext(forecasts -> cityDao.insert(CityConverter.toEntity(forecasts.city())))
                .flatMap(forecasts -> Flowable.fromIterable(forecasts.forecasts())
                        .map(forecast -> ForecastConverter.toEntity(forecast, forecasts.city()))
                        .toList()
                        .toFlowable()
                        .filter(entities -> !entities.isEmpty())
                        .doOnNext(forecastDao::insert));
    }
}
