package com.weatherforecast.features.dailyforecast.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.common.data.converter.ForecastConverter;
import com.weatherforecast.features.common.data.repository.ForecastDao;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;

import java.util.List;

import io.reactivex.Flowable;

public class FetchLocalForecastUseCase extends UseCase<List<DailyForecast>, Long> {

    private final ForecastDao forecastDao;

    public FetchLocalForecastUseCase(@NonNull final ExecutionConfiguration configuration,
                                     @NonNull final WeatherForecastDatabase database) {
        super(configuration);
        this.forecastDao = database.forecastDao();
    }

    @Override
    public Flowable<List<DailyForecast>> buildUseCaseObservable(@Nullable final Long cityId) {
        if (cityId == null) {
            return Flowable.empty();
        }

        return forecastDao.findForecastForCity(cityId)
                .flatMap(entities ->
                        Flowable.fromIterable(entities)
                                .map(ForecastConverter::fromEntity)
                                .sorted((forecast1, forecast2) -> forecast1.date().compareTo(forecast2.date()))
                                .groupBy(forecast -> forecast.date().substring(0, 10))
                                .takeLast(5)
                                .flatMap(grouped -> grouped
                                        .buffer(8)
                                        .map(forecasts -> DailyForecast.create(grouped.getKey(), forecasts))
                                )
                                .toList()
                                .filter(values -> !values.isEmpty())
                                .toFlowable()
                );
    }

}
