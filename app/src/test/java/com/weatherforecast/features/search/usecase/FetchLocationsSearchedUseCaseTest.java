package com.weatherforecast.features.search.usecase;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.features.common.data.converter.CityConverter;
import com.weatherforecast.features.common.data.entity.CityEntity;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.common.data.repository.CityDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import io.reactivex.Flowable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchLocationsSearchedUseCaseTest {

    @Mock
    private CityDao cityDao;

    private FetchLocationsSearchedUseCase useCase;

    @Before
    public void setup() {
        final ExecutionConfiguration configuration = mock(ExecutionConfiguration.class);
        final WeatherForecastDatabase database = mock(WeatherForecastDatabase.class);
        when(database.cityDao()).thenReturn(cityDao);
        useCase = new FetchLocationsSearchedUseCase(configuration, database);
    }

    @Test
    public void verifyEmptyResultWhenDatabaseIsEmpty() {
        when(cityDao.all()).thenReturn(Flowable.empty());

        useCase.buildUseCaseObservable(null).test()
                .assertNoErrors()
                .assertComplete()
                .assertNoValues();
    }

    @Test
    public void verifyModelReturnedOrderedByNameWhenDatabaseNotEmpty() {
        final City city1 = City.create(1L, "City 1");
        final City city2 = City.create(2L, "City 2");
        final City city3 = City.create(3L, "City 3");

        final CityEntity entity1 = CityConverter.toEntity(city1);
        final CityEntity entity2 = CityConverter.toEntity(city2);
        final CityEntity entity3 = CityConverter.toEntity(city3);

        when(cityDao.all()).thenReturn(Flowable.just(
                Arrays.asList(entity3, entity1, entity2)));

        useCase.buildUseCaseObservable(null).test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(Arrays.asList(city1, city2, city3));
    }

}
