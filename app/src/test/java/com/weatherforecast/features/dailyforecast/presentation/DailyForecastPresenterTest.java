package com.weatherforecast.features.dailyforecast.presentation;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import com.weatherforecast.features.common.data.helper.TestDataCreator;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;
import com.weatherforecast.features.dailyforecast.usecase.FetchLocalForecastUseCase;
import com.weatherforecast.features.dailyforecast.usecase.UpdateLocalForecastUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Subscription;

import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class DailyForecastPresenterTest {

    @Mock
    private DailyForecastContract.View view;

    @Mock
    private DailyForecastDataHolder dataHolder;

    @Mock
    private FetchLocalForecastUseCase fetchLocalUseCase;

    @Mock
    private UpdateLocalForecastUseCase updateLocalUseCase;

    private DailyForecastPresenter presenter;

    @Before
    public void setup() {
        when(view.provideForecastsDataHolder()).thenReturn(dataHolder);
        when(view.provideLifecycleOwner()).thenReturn(mock(LifecycleOwner.class));
        presenter = new DailyForecastPresenter(view, fetchLocalUseCase, updateLocalUseCase);
    }

    @Test
    public void assertContentIsNotAvailableOnHolderWhenLiveDataIsNull() {
        when(dataHolder.data()).thenReturn(null);
        assertFalse(presenter.isContentAvailableOnHolder(dataHolder));
    }

    @Test
    public void assertContentIsNotAvailableOnHolderWhenLiveDataValueIsNull() {
        final LiveData<List<DailyForecast>> data = mock(LiveData.class);
        when(data.getValue()).thenReturn(null);
        when(dataHolder.data()).thenReturn(data);
        assertFalse(presenter.isContentAvailableOnHolder(dataHolder));
    }

    @Test
    public void assertDataIsAvailableOnHolder() {
        final LiveData<List<DailyForecast>> data = mock(LiveData.class);
        final List<DailyForecast> value = Collections.emptyList();
        when(data.getValue()).thenReturn(value);
        when(dataHolder.data()).thenReturn(data);
        assertTrue(presenter.isContentAvailableOnHolder(dataHolder));
    }

    @Test
    public void verifyDataDisplayedWhenAvailableOnHolder() {
        final DailyForecast dailyForecast = TestDataCreator
                .createDailyForecastWithDateAndTimes("10-01-2017", Collections.singletonList("12:00:00"));

        final LiveData<List<DailyForecast>> data = mock(LiveData.class);
        final List<DailyForecast> value = Collections.singletonList(dailyForecast);
        when(data.getValue()).thenReturn(value);
        when(dataHolder.data()).thenReturn(data);

        presenter.loadLocationForecast(0L);

        verify(view, times(1)).showDailyForecasts(value);
        verify(fetchLocalUseCase, never()).executeLive(anyLong(), any(), any(), any());
        verify(updateLocalUseCase, never()).execute(anyLong(), any(), any(), any());
    }

    @Test
    public void verifyDataLoadedAndUpdatedWhenNotAvailableOnHolder() {
        when(dataHolder.data()).thenReturn(null);
        when(fetchLocalUseCase.executeLive(anyLong(), any(), any(), any())).thenReturn(mock(LiveData.class));
        when(updateLocalUseCase.execute(anyLong(), any(), any(), any())).thenReturn(Flowable.empty());

        presenter.loadLocationForecast(0L);

        verify(view, never()).showDailyForecasts(any());
        verify(fetchLocalUseCase, times(1)).executeLive(anyLong(), any(), any(), any());
        verify(updateLocalUseCase, times(1)).execute(anyLong(), any(), any(), any());
    }

    @Test
    public void verifyFetchLocalUseCaseSubscriptionDeliveredToHolder() throws Exception {
        final LiveData<List<DailyForecast>> data = mock(LiveData.class);
        when(fetchLocalUseCase.executeLive(anyLong(), any(), any(), any())).thenReturn(data);

        presenter.loadLocalForecast(0L, dataHolder);

        final ArgumentCaptor<Consumer<Subscription>> captor = ArgumentCaptor.forClass(Consumer.class);
        verify(fetchLocalUseCase, times(1)).executeLive(eq(0L), captor.capture(), any(), any());

        final Subscription subscription = mock(Subscription.class);
        final Consumer<Subscription> consumer = captor.getAllValues().get(0);
        consumer.accept(subscription);
        verify(dataHolder, times(1)).addSubscription(subscription);
    }

    @Test
    public void verifyLocalDataLoadedWithError() throws Exception {
        final LiveData<List<DailyForecast>> data = mock(LiveData.class);
        when(fetchLocalUseCase.executeLive(anyLong(), any(), any(), any())).thenReturn(data);

        presenter.loadLocalForecast(0L, dataHolder);

        final ArgumentCaptor<Consumer<Throwable>> captor = ArgumentCaptor.forClass(Consumer.class);
        verify(fetchLocalUseCase, times(1)).executeLive(eq(0L), any(), captor.capture(), any());

        final Consumer<Throwable> consumer = captor.getAllValues().get(0);
        consumer.accept(new Throwable());
        verify(view, times(1)).showErrorLoadingDailyForecast();
    }

    @Test
    public void verifyLocalDataLoadedWithSuccess() throws Exception {
        final LiveData<List<DailyForecast>> data = mock(LiveData.class);
        when(fetchLocalUseCase.executeLive(anyLong(), any(), any(), any())).thenReturn(data);

        presenter.loadLocalForecast(0L, dataHolder);
        verify(fetchLocalUseCase, times(1)).executeLive(eq(0L), any(), any(), any());

        final ArgumentCaptor<Observer<List<DailyForecast>>> captor = ArgumentCaptor.forClass(Observer.class);
        verify(data, times(1)).observe(any(), captor.capture());

        final Observer<List<DailyForecast>> observer = captor.getAllValues().get(0);
        final List<DailyForecast> content = Collections.emptyList();
        observer.onChanged(content);
        verify(view, times(1)).showDailyForecasts(content);
    }

    @Test
    public void verifyUpdateUseCaseSubscriptionDeliveredToHolder() throws Exception {
        when(updateLocalUseCase.execute(anyLong(), any(), any(), any())).thenReturn(Flowable.empty());

        presenter.updateRemoteForecast(0L, dataHolder);

        final ArgumentCaptor<Consumer<Subscription>> captor = ArgumentCaptor.forClass(Consumer.class);
        verify(updateLocalUseCase, times(1)).execute(eq(0L), captor.capture(), any(), any());

        final Subscription subscription = mock(Subscription.class);
        final Consumer<Subscription> consumer = captor.getAllValues().get(0);
        consumer.accept(subscription);
        verify(dataHolder, times(1)).addSubscription(subscription);
    }

    @Test
    public void verifyLocalDataUpdatedWithError() throws Exception {
        when(updateLocalUseCase.execute(anyLong(), any(), any(), any())).thenReturn(Flowable.empty());

        presenter.updateRemoteForecast(0L, dataHolder);

        final ArgumentCaptor<Consumer<Throwable>> captor = ArgumentCaptor.forClass(Consumer.class);
        verify(updateLocalUseCase, times(1)).execute(eq(0L), any(), captor.capture(), any());

        final Consumer<Throwable> consumer = captor.getAllValues().get(0);
        consumer.accept(new Throwable());
        verify(view, times(1)).showErrorLoadingDailyForecast();
    }

}
