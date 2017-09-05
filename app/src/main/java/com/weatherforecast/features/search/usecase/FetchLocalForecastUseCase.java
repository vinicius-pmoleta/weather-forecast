package com.weatherforecast.features.search.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.common.data.converter.ForecastConverter;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.common.data.repository.ForecastDao;

import java.util.List;

import io.reactivex.Flowable;

public class FetchLocalForecastUseCase extends UseCase<List<Forecast>, Long> {

    private final ForecastDao forecastDao;

    public FetchLocalForecastUseCase(@NonNull final ExecutionConfiguration configuration,
                                     @NonNull final WeatherForecastDatabase database) {
        super(configuration);
        this.forecastDao = database.forecastDao();
    }

    @Override
    public Flowable<List<Forecast>> buildUseCaseObservable(@Nullable final Long cityId) {
        if (cityId == null) {
            return Flowable.empty();
        }
        return forecastDao.findForecastForCity(cityId)
                .flatMap(entities ->
                        Flowable.fromIterable(entities)
                                .map(ForecastConverter::fromEntity)
                                .toList().toFlowable()
                );
    }

}
