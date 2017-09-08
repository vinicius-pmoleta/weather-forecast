package com.weatherforecast.features.search.usecase;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.repository.remote.ForecastRepository;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.features.common.data.entity.CityEntity;
import com.weatherforecast.features.common.data.helper.TestDataCreator;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.common.data.repository.CityDao;
import com.weatherforecast.features.search.data.Weather;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Flowable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchWeatherUseCaseTest {

    @Mock
    private ForecastRepository forecastsRepository;

    @Mock
    private CityDao cityDao;

    private FetchWeatherUseCase useCase;

    @Before
    public void setup() {
        final ExecutionConfiguration configuration = mock(ExecutionConfiguration.class);
        final WeatherForecastDatabase database = mock(WeatherForecastDatabase.class);
        when(database.cityDao()).thenReturn(cityDao);
        useCase = new FetchWeatherUseCase(forecastsRepository, configuration, database);
    }

    @Test
    public void verifyResultIsEmptyWhenNoRemoteWeatherFound() {
        when(forecastsRepository.getWeather(anyString())).thenReturn(Flowable.empty());

        useCase.buildUseCaseObservable(anyString())
                .doOnComplete(() -> verify(cityDao, never()).insert(any(CityEntity.class)))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertNoValues();
    }

    @Test
    public void verifyResultCityPersistedWhenRemoteWeatherFound() {
        final Forecast forecast = TestDataCreator.createForecastModelWithDataAndTime("10-01-2017", "12:00:00");
        final Weather weather = Weather.create(1L, "City", forecast.temperature(), forecast.conditions());

        when(forecastsRepository.getWeather(anyString())).thenReturn(Flowable.just(weather));

        useCase.buildUseCaseObservable(anyString())
                .doOnComplete(() -> verify(cityDao, times(1)).insert(any(CityEntity.class)))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(weather);
    }

    @Test
    public void verifyResultIsEmptyOnRemoteWeatherError() {
        final RuntimeException error = new RuntimeException("Test");
        when(forecastsRepository.getWeather(anyString())).thenReturn(Flowable.error(error));

        useCase.buildUseCaseObservable(anyString())
                .test()
                .assertError(error)
                .assertNotComplete()
                .assertNoValues();
    }

}
