package com.weatherforecast.features.search.usecase;

import com.weatherforecast.core.data.repository.local.database.WeatherForecastDatabase;
import com.weatherforecast.core.data.usecase.ExecutionConfiguration;
import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.helper.TestDataCreator;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchLocalForecastUseCaseTest {

    @Mock
    private ForecastDao forecastDao;

    private FetchLocalForecastUseCase useCase;

    @Before
    public void setup() {
        final WeatherForecastDatabase database = mock(WeatherForecastDatabase.class);
        when(database.forecastDao()).thenReturn(forecastDao);
        final ExecutionConfiguration configuration = mock(ExecutionConfiguration.class);
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
        final ForecastEntity entity1 = TestDataCreator.createEntityWithDateAndCityId("10-01-2017 12:00:00", 0L);
        final ForecastEntity entity2 = TestDataCreator.createEntityWithDateAndCityId("11-01-2017 12:00:00", 0L);
        final ForecastEntity entity3 = TestDataCreator.createEntityWithDateAndCityId("12-01-2017 12:00:00", 0L);
        final ForecastEntity entity4 = TestDataCreator.createEntityWithDateAndCityId("10-01-2017 09:00:00", 0L);

        when(forecastDao.findForecastForCity(anyLong())).thenReturn(
                Flowable.just(Arrays.asList(entity2, entity3, entity1, entity4)));

        final DailyForecast dailyForecast1 = TestDataCreator.createDailyForecastWithDateAndTimes("10-01-2017", Arrays.asList("09:00:00", "12:00:00"));
        final DailyForecast dailyForecast2 = TestDataCreator.createDailyForecastWithDateAndTimes("11-01-2017", Collections.singletonList("12:00:00"));
        final DailyForecast dailyForecast3 = TestDataCreator.createDailyForecastWithDateAndTimes("12-01-2017", Collections.singletonList("12:00:00"));

        useCase.buildUseCaseObservable(0L).test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(Arrays.asList(dailyForecast1, dailyForecast2, dailyForecast3));
    }

    @Test
    public void verifyResultsAreGroupedByDateWithoutTime() {
        final ForecastEntity entity1 = TestDataCreator.createEntityWithDateAndCityId("10-01-2017 09:00:00", 0L);
        final ForecastEntity entity2 = TestDataCreator.createEntityWithDateAndCityId("10-01-2017 12:00:00", 0L);
        final ForecastEntity entity3 = TestDataCreator.createEntityWithDateAndCityId("10-01-2017 15:00:00", 0L);

        when(forecastDao.findForecastForCity(anyLong())).thenReturn(
                Flowable.just(Arrays.asList(entity2, entity3, entity1)));

        final DailyForecast dailyForecast = TestDataCreator.createDailyForecastWithDateAndTimes("10-01-2017", Arrays.asList("09:00:00", "12:00:00", "15:00:00"));

        useCase.buildUseCaseObservable(0L).test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(Collections.singletonList(dailyForecast));
    }

    @Test
    public void verifyResultsAreOnlyFromLastFiveDays() {
        final ForecastEntity entity1 = TestDataCreator.createEntityWithDateAndCityId("08-01-2017 12:00:00", 0L);
        final ForecastEntity entity2 = TestDataCreator.createEntityWithDateAndCityId("09-01-2017 12:00:00", 0L);
        final ForecastEntity entity3 = TestDataCreator.createEntityWithDateAndCityId("10-01-2017 12:00:00", 0L);
        final ForecastEntity entity4 = TestDataCreator.createEntityWithDateAndCityId("11-01-2017 12:00:00", 0L);
        final ForecastEntity entity5 = TestDataCreator.createEntityWithDateAndCityId("12-01-2017 12:00:00", 0L);
        final ForecastEntity entity6 = TestDataCreator.createEntityWithDateAndCityId("13-01-2017 12:00:00", 0L);
        final ForecastEntity entity7 = TestDataCreator.createEntityWithDateAndCityId("14-01-2017 12:00:00", 0L);

        when(forecastDao.findForecastForCity(anyLong())).thenReturn(
                Flowable.just(Arrays.asList(entity2, entity3, entity1, entity4, entity5, entity7, entity6)));

        final DailyForecast dailyForecast1 = TestDataCreator.createDailyForecastWithDateAndTimes("10-01-2017", Collections.singletonList("12:00:00"));
        final DailyForecast dailyForecast2 = TestDataCreator.createDailyForecastWithDateAndTimes("11-01-2017", Collections.singletonList("12:00:00"));
        final DailyForecast dailyForecast3 = TestDataCreator.createDailyForecastWithDateAndTimes("12-01-2017", Collections.singletonList("12:00:00"));
        final DailyForecast dailyForecast4 = TestDataCreator.createDailyForecastWithDateAndTimes("13-01-2017", Collections.singletonList("12:00:00"));
        final DailyForecast dailyForecast5 = TestDataCreator.createDailyForecastWithDateAndTimes("14-01-2017", Collections.singletonList("12:00:00"));

        useCase.buildUseCaseObservable(0L).test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(Arrays.asList(dailyForecast1, dailyForecast2, dailyForecast3, dailyForecast4, dailyForecast5));
    }

}
