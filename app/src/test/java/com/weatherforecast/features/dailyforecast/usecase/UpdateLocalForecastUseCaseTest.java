package com.weatherforecast.features.dailyforecast.usecase;

import android.support.annotation.NonNull;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.features.common.data.entity.CityEntity;
import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.helper.TestDataCreator;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.common.data.model.Forecasts;
import com.weatherforecast.features.common.data.repository.CityDao;
import com.weatherforecast.features.common.data.repository.ForecastDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class UpdateLocalForecastUseCaseTest {

    @Mock
    private ForecastRepository forecastRepository;

    @Mock
    private ForecastDao forecastDao;

    @Mock
    private CityDao cityDao;

    private UpdateLocalForecastUseCase useCase;

    @Before
    public void setup() {
        final WeatherForecastDatabase database = mock(WeatherForecastDatabase.class);
        when(database.forecastDao()).thenReturn(forecastDao);
        when(database.cityDao()).thenReturn(cityDao);
        final ExecutionConfiguration configuration = mock(ExecutionConfiguration.class);
        useCase = new UpdateLocalForecastUseCase(forecastRepository, configuration, database);
    }

    @Test
    public void verifyNoDatabaseInteractionWhenRemoteDataIsEmpty() {
        when(forecastRepository.getForecast(anyString())).thenReturn(Flowable.empty());

        useCase.buildUseCaseObservable(anyString())
                .doOnComplete(() -> {
                    verify(cityDao, never()).insert(any(CityEntity.class));
                    verify(forecastDao, never()).insert(any(List.class));
                })
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertNoValues();
    }

    @Test
    public void verifyCityInsertedInDatabaseEvenWithoutForecasts() {
        final City city = City.create(0L, "City");
        final Forecasts forecasts = Forecasts.create(Collections.emptyList(), city);

        when(forecastRepository.getForecast(anyString())).thenReturn(Flowable.just(forecasts));

        useCase.buildUseCaseObservable(anyString())
                .doOnComplete(() -> {
                    assertCityEntityInserted(city);
                    verify(forecastDao, never()).insert(any(List.class));
                })
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertNoValues();
    }

    @Test
    public void verifyCityAndForecastsInsertedInDatabase() {
        final City city = City.create(0L, "City");
        final Forecast forecast1 = TestDataCreator.createForecastModelWithDataAndTime("10-01-2017", "09:00:00");
        final Forecast forecast2 = TestDataCreator.createForecastModelWithDataAndTime("10-01-2017", "12:00:00");

        final Forecasts forecasts = Forecasts.create(Arrays.asList(forecast1, forecast2), city);

        final ForecastEntity entity1 = TestDataCreator.createEntityWithDateAndCityId(forecast1.date(), city.id());
        final ForecastEntity entity2 = TestDataCreator.createEntityWithDateAndCityId(forecast1.date(), city.id());
        final List<ForecastEntity> expectedEntities = Arrays.asList(entity1, entity2);

        when(forecastRepository.getForecast(anyString())).thenReturn(Flowable.just(forecasts));

        useCase.buildUseCaseObservable(anyString())
                .doOnComplete(() -> {
                    assertCityEntityInserted(city);
                    verify(forecastDao, times(1)).insert(expectedEntities);
                })
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValues(expectedEntities);
    }

    private void assertCityEntityInserted(@NonNull final City city) {
        final ArgumentCaptor<CityEntity> captor = ArgumentCaptor.forClass(CityEntity.class);
        verify(cityDao, times(1)).insert(captor.capture());

        final CityEntity entity = captor.getAllValues().get(0);
        assertNotNull(entity);
        assertEquals(city.id(), entity.id);
        assertEquals(city.name(), entity.name);
    }

}
