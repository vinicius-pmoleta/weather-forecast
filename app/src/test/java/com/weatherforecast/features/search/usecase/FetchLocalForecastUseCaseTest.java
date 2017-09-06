package com.weatherforecast.features.search.usecase;

import android.support.annotation.NonNull;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.repository.ForecastDao;
import com.weatherforecast.features.search.data.model.DailyForecast;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Flowable;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchLocalForecastUseCaseTest {

    @Mock
    private ExecutionConfiguration configuration;

    @Mock
    private WeatherForecastDatabase database;

    @Mock
    private ForecastDao forecastDao;

    private FetchLocalForecastUseCase useCase;

    @Before
    public void setup() {
        when(database.forecastDao()).thenReturn(forecastDao);
        useCase = new FetchLocalForecastUseCase(configuration, database);
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
    public void verifyResultIsEmptyWhenDatabaseIsEmpty() {
        when(forecastDao.findForecastForCity(anyLong())).thenReturn(Flowable.empty());
        useCase.buildUseCaseObservable(0L).test()
                .assertNoErrors()
                .assertComplete()
                .assertNoValues();
    }

    @Test
    public void verifyResultsAreOrderedByDateAsc() {
        final ForecastEntity entity1 = createEntityWithDate("10-01-2017 12:00:00");
        final ForecastEntity entity2 = createEntityWithDate("11-01-2017 12:00:00");
        final ForecastEntity entity3 = createEntityWithDate("12-01-2017 12:00:00");

        when(forecastDao.findForecastForCity(anyLong())).thenReturn(
                Flowable.just(Arrays.asList(entity2, entity3, entity1)));

        final DailyForecast dailyForecast1 = createDailyForecastWithDate("10-01-2017");
        final DailyForecast dailyForecast2 = createDailyForecastWithDate("11-01-2017");
        final DailyForecast dailyForecast3 = createDailyForecastWithDate("12-01-2017");

        useCase.buildUseCaseObservable(0L).test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(Arrays.asList(dailyForecast1, dailyForecast2, dailyForecast3));
    }

    @NonNull
    private ForecastEntity createEntityWithDate(@NonNull final String date) {
        final ForecastEntity entity = new ForecastEntity();
        entity.date = date;
        return entity;
    }

    @NonNull
    private DailyForecast createDailyForecastWithDate(@NonNull final String date) {
        return new DailyForecast(date, Collections.emptyList());
    }

}
