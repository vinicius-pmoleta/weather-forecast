package com.weatherforecast.features.search.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.common.data.converter.CityConverter;
import com.weatherforecast.features.common.data.converter.ForecastConverter;
import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.common.data.repository.CityDao;
import com.weatherforecast.features.common.data.repository.ForecastDao;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

public class FetchRemoteForecastUseCase extends UseCase<List<Forecast>, String> {

    private final ForecastRepository repository;
    private final CityDao cityDao;
    private final ForecastDao forecastDao;

    public FetchRemoteForecastUseCase(@NonNull final ForecastRepository repository,
                                      @NonNull final ExecutionConfiguration configuration,
                                      @NonNull final WeatherForecastDatabase database) {
        super(configuration);
        this.repository = repository;
        this.cityDao = database.cityDao();
        this.forecastDao = database.forecastDao();
    }

    @Override
    public Flowable<List<Forecast>> buildUseCaseObservable(@Nullable final String location) {
        return repository.getForecast(location)
                .delay(5, TimeUnit.SECONDS)
                .flatMap(forecasts -> {
                    cityDao.insert(CityConverter.toEntity(forecasts.city()));

                    return Flowable.fromIterable(forecasts.forecasts())
                            .map(forecast -> {
                                final ForecastEntity entity = ForecastConverter.toEntity(forecast, forecasts.city());
                                forecastDao.insert(entity);
                                return forecast;
                            })
                            .toList().toFlowable();
                });
    }
}
