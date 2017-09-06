package com.weatherforecast.features.search.usecase;

import android.support.annotation.NonNull;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.common.data.repository.ForecastDao;
import com.weatherforecast.features.search.data.model.DailyForecast;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public void verifyResultsAreOrderedAndGroupedByDateAsc() {
        final ForecastEntity entity1 = createEntityWithDate("10-01-2017 12:00:00");
        final ForecastEntity entity2 = createEntityWithDate("11-01-2017 12:00:00");
        final ForecastEntity entity3 = createEntityWithDate("12-01-2017 12:00:00");
        final ForecastEntity entity4 = createEntityWithDate("10-01-2017 09:00:00");

        when(forecastDao.findForecastForCity(anyLong())).thenReturn(
                Flowable.just(Arrays.asList(entity2, entity3, entity1, entity4)));

        final DailyForecast dailyForecast1 = createDailyForecastWithDateAndTimes("10-01-2017", Arrays.asList("09:00:00", "12:00:00"));
        final DailyForecast dailyForecast2 = createDailyForecastWithDateAndTimes("11-01-2017", Collections.singletonList("12:00:00"));
        final DailyForecast dailyForecast3 = createDailyForecastWithDateAndTimes("12-01-2017", Collections.singletonList("12:00:00"));

        useCase.buildUseCaseObservable(0L).test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(Arrays.asList(dailyForecast1, dailyForecast2, dailyForecast3));
    }

    @Test
    public void verifyResultsAreGroupedByDateWithoutTime() {
        final ForecastEntity entity1 = createEntityWithDate("10-01-2017 09:00:00");
        final ForecastEntity entity2 = createEntityWithDate("10-01-2017 12:00:00");
        final ForecastEntity entity3 = createEntityWithDate("10-01-2017 15:00:00");

        when(forecastDao.findForecastForCity(anyLong())).thenReturn(
                Flowable.just(Arrays.asList(entity2, entity3, entity1)));

        final DailyForecast dailyForecast = createDailyForecastWithDateAndTimes("10-01-2017", Arrays.asList("09:00:00", "12:00:00", "15:00:00"));

        useCase.buildUseCaseObservable(0L).test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(Collections.singletonList(dailyForecast));
    }

    @Test
    public void verifyResultsAreOnlyFromLastFiveDays() {
        final ForecastEntity entity1 = createEntityWithDate("08-01-2017 12:00:00");
        final ForecastEntity entity2 = createEntityWithDate("09-01-2017 12:00:00");
        final ForecastEntity entity3 = createEntityWithDate("10-01-2017 12:00:00");
        final ForecastEntity entity4 = createEntityWithDate("11-01-2017 12:00:00");
        final ForecastEntity entity5 = createEntityWithDate("12-01-2017 12:00:00");
        final ForecastEntity entity6 = createEntityWithDate("13-01-2017 12:00:00");
        final ForecastEntity entity7 = createEntityWithDate("14-01-2017 12:00:00");

        when(forecastDao.findForecastForCity(anyLong())).thenReturn(
                Flowable.just(Arrays.asList(entity2, entity3, entity1, entity4, entity5, entity7, entity6)));

        final DailyForecast dailyForecast1 = createDailyForecastWithDateAndTimes("10-01-2017", Collections.singletonList("12:00:00"));
        final DailyForecast dailyForecast2 = createDailyForecastWithDateAndTimes("11-01-2017", Collections.singletonList("12:00:00"));
        final DailyForecast dailyForecast3 = createDailyForecastWithDateAndTimes("12-01-2017", Collections.singletonList("12:00:00"));
        final DailyForecast dailyForecast4 = createDailyForecastWithDateAndTimes("13-01-2017", Collections.singletonList("12:00:00"));
        final DailyForecast dailyForecast5 = createDailyForecastWithDateAndTimes("14-01-2017", Collections.singletonList("12:00:00"));

        useCase.buildUseCaseObservable(0L).test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(Arrays.asList(dailyForecast1, dailyForecast2, dailyForecast3, dailyForecast4, dailyForecast5));
    }

    @NonNull
    private ForecastEntity createEntityWithDate(@NonNull final String date) {
        final ForecastEntity entity = new ForecastEntity();
        entity.date = date;
        entity.cityId = 0L;
        entity.currentTemperature = 20.0;
        entity.minimumTemperature = 10.0;
        entity.maximumTemperature = 30.0;
        entity.condition = "Condition";
        entity.icon = "condition-00";
        return entity;
    }

    @NonNull
    private DailyForecast createDailyForecastWithDateAndTimes(@NonNull final String date, @NonNull final List<String> times) {
        final List<Forecast> forecasts = new ArrayList<>();
        for (final String time : times) {
            final Forecast.Temperature temperature = Forecast.Temperature.create(20.0, 10.0, 30.0);
            final Forecast.Condition condition = Forecast.Condition.create("Condition", "condition-00");
            final Forecast forecast = Forecast.create(date + " " + time, temperature, Collections.singletonList(condition));
            forecasts.add(forecast);
        }
        return DailyForecast.create(date, forecasts);
    }

}
