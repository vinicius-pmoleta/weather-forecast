package com.weatherforecast.features.dailyforecast.usecase;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.helper.TestDataCreator;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.common.data.model.Forecasts;
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
import static org.mockito.ArgumentMatchers.anyLong;
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

    private UpdateLocalForecastUseCase useCase;

    @Before
    public void setup() {
        final WeatherForecastDatabase database = mock(WeatherForecastDatabase.class);
        when(database.forecastDao()).thenReturn(forecastDao);
        final ExecutionConfiguration configuration = mock(ExecutionConfiguration.class);
        useCase = new UpdateLocalForecastUseCase(forecastRepository, configuration, database);
    }

    @Test
    public void verifyResultIsEmptyWhenCityIdIsNotAvailable() {
        useCase.buildUseCaseObservable(null)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertNoValues();
    }

    @Test
    public void verifyNoDatabaseInteractionWhenRemoteDataIsEmpty() {
        when(forecastRepository.getForecast(anyLong())).thenReturn(Flowable.empty());

        useCase.buildUseCaseObservable(anyLong())
                .doOnComplete(() -> verify(forecastDao, never()).insert(any(List.class)))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertNoValues();
    }

    @Test
    public void verifyCityInsertedInDatabaseEvenWithoutForecasts() {
        final City city = City.create(0L, "City");
        final Forecasts forecasts = Forecasts.create(Collections.emptyList(), city);

        when(forecastRepository.getForecast(anyLong())).thenReturn(Flowable.just(forecasts));

        useCase.buildUseCaseObservable(anyLong())
                .doOnComplete(() -> verify(forecastDao, never()).insert(any(List.class)))
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
        when(forecastRepository.getForecast(anyLong())).thenReturn(Flowable.just(forecasts));

        useCase.buildUseCaseObservable(anyLong())
                .doOnComplete(this::verifyForecastEntitiesSaved)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValues(Arrays.asList(forecast1, forecast2));
    }

    private void verifyForecastEntitiesSaved() {
        final ArgumentCaptor<List<ForecastEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(forecastDao, times(1)).insert(captor.capture());

        final List<ForecastEntity> entities = captor.getAllValues().get(0);
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals(0L, entities.get(0).cityId);
        assertEquals("10-01-2017 09:00:00", entities.get(0).date);
        assertEquals(0L, entities.get(1).cityId);
        assertEquals("10-01-2017 12:00:00", entities.get(1).date);
    }

}
