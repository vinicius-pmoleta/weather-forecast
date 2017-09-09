package com.weatherforecast.features.search.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.common.data.converter.CityConverter;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.common.data.repository.CityDao;

import java.util.List;

import io.reactivex.Flowable;

public class FetchLocationsSearchedUseCase extends UseCase<List<City>, Void> {

    private final CityDao cityDao;

    public FetchLocationsSearchedUseCase(@NonNull final ExecutionConfiguration configuration,
                                         @NonNull final WeatherForecastDatabase database) {
        super(configuration);
        this.cityDao = database.cityDao();
    }

    @Override
    public Flowable<List<City>> buildUseCaseObservable(@Nullable final Void param) {
        return cityDao.all()
                .flatMap(cities -> Flowable.fromIterable(cities)
                        .map(CityConverter::fromEntity)
                        .sorted((city1, city2) -> city1.name().compareTo(city2.name()))
                        .toList()
                        .toFlowable()
                );
    }

}
