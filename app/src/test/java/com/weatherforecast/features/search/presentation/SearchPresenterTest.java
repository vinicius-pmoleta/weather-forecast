package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import com.weatherforecast.core.data.live.LiveResult;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.search.data.Weather;
import com.weatherforecast.features.search.presentation.model.WeatherScreenConverter;
import com.weatherforecast.features.search.presentation.model.WeatherScreenModel;
import com.weatherforecast.features.search.usecase.FetchLocationsSearchedUseCase;
import com.weatherforecast.features.search.usecase.FetchWeatherUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.functions.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class SearchPresenterTest {

    @Mock
    private SearchContract.View view;

    @Mock
    private SearchDataHolder dataHolder;

    @Mock
    private FetchWeatherUseCase fetchWeatherUseCase;

    @Mock
    private FetchLocationsSearchedUseCase fetchSearchesUseCase;

    @Mock
    private WeatherScreenConverter screenConverter;

    private SearchPresenter presenter;

    @Before
    public void setup() {
        when(view.provideSearchDataHolder()).thenReturn(dataHolder);
        when(view.provideLifecycleOwner()).thenReturn(mock(LifecycleOwner.class));
        presenter = new SearchPresenter(view, fetchWeatherUseCase, fetchSearchesUseCase, screenConverter);
    }

    @Test
    public void verifyFetchWeatherUseCaseSubscriptionDeliveredToHolder() throws Exception {
        final LiveResult<Weather> result = mock(LiveResult.class);
        when(fetchWeatherUseCase.executeLive(anyString(), any(), any())).thenReturn(result);

        presenter.loadRemoteWeather("Location", dataHolder);

        final ArgumentCaptor<Consumer<Subscription>> captor = ArgumentCaptor.forClass(Consumer.class);
        verify(fetchWeatherUseCase, times(1)).executeLive(anyString(), captor.capture(), any());

        final Subscription subscription = mock(Subscription.class);
        final Consumer<Subscription> consumer = captor.getAllValues().get(0);
        consumer.accept(subscription);
        verify(dataHolder, times(1)).addSubscription(subscription);
    }

    @Test
    public void verifyRemoteWeatherLoadedWithError() throws Exception {
        final LiveResult<Weather> result = mock(LiveResult.class);
        when(fetchWeatherUseCase.executeLive(anyString(), any(), any())).thenReturn(result);

        presenter.loadRemoteWeather("Location", dataHolder);
        verify(fetchWeatherUseCase, times(1)).executeLive(anyString(), any(), any());

        final ArgumentCaptor<Observer<Throwable>> captor = ArgumentCaptor.forClass(Observer.class);
        verify(result, times(1)).observe(any(), any(), captor.capture());

        final Observer<Throwable> observer = captor.getAllValues().get(0);
        observer.onChanged(new Throwable());
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).showErrorLoadingWeather();
    }

    @Test
    public void verifyRemoteWeatherLoadedWithSuccess() throws Exception {
        final LiveResult<Weather> result = mock(LiveResult.class);
        when(fetchWeatherUseCase.executeLive(anyString(), any(), any())).thenReturn(result);

        presenter.loadRemoteWeather("Location", dataHolder);
        verify(fetchWeatherUseCase, times(1)).executeLive(anyString(), any(), any());
        verify(dataHolder, times(1)).weatherResult(result);

        final ArgumentCaptor<Observer<Weather>> captor = ArgumentCaptor.forClass(Observer.class);
        verify(result, times(1)).observe(any(), captor.capture(), any());

        final Observer<Weather> observer = captor.getAllValues().get(0);
        final Weather content = mock(Weather.class);
        final WeatherScreenModel contentModel = mock(WeatherScreenModel.class);
        when(screenConverter.prepareForPresentation(content)).thenReturn(contentModel);

        observer.onChanged(content);
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).showWeather(contentModel);
    }

    @Test
    public void verifyFetchSearchesUseCaseSubscriptionDeliveredToHolder() throws Exception {
        final LiveResult<List<City>> result = mock(LiveResult.class);
        when(fetchSearchesUseCase.executeLive(any(), any(), any())).thenReturn(result);

        presenter.loadLocalSearches(dataHolder);

        final ArgumentCaptor<Consumer<Subscription>> captor = ArgumentCaptor.forClass(Consumer.class);
        verify(fetchSearchesUseCase, times(1)).executeLive(any(), captor.capture(), any());

        final Subscription subscription = mock(Subscription.class);
        final Consumer<Subscription> consumer = captor.getAllValues().get(0);
        consumer.accept(subscription);
        verify(dataHolder, times(1)).addSubscription(subscription);
    }

    @Test
    public void verifyLocalSearchesLoadedWithError() throws Exception {
        final LiveResult<List<City>> result = mock(LiveResult.class);
        when(fetchSearchesUseCase.executeLive(any(), any(), any())).thenReturn(result);

        presenter.loadLocalSearches(dataHolder);
        verify(fetchSearchesUseCase, times(1)).executeLive(any(), any(), any());
        verify(dataHolder, times(1)).locationSearchesResult(result);

        final ArgumentCaptor<Observer<Throwable>> captor = ArgumentCaptor.forClass(Observer.class);
        verify(result, times(1)).observe(any(), any(), captor.capture());

        final Observer<Throwable> observer = captor.getAllValues().get(0);
        observer.onChanged(new Throwable());
        verify(view, times(1)).showErrorLoadingWeather();
    }

    @Test
    public void verifyLocalSearchesLoadedWithSuccess() throws Exception {
        final LiveResult<List<City>> result = mock(LiveResult.class);
        when(fetchSearchesUseCase.executeLive(any(), any(), any())).thenReturn(result);

        presenter.loadLocalSearches(dataHolder);
        verify(fetchSearchesUseCase, times(1)).executeLive(any(), any(), any());
        verify(dataHolder, times(1)).locationSearchesResult(result);

        final ArgumentCaptor<Observer<List<City>>> captor = ArgumentCaptor.forClass(Observer.class);
        verify(result, times(1)).observe(any(), captor.capture(), any());

        final Observer<List<City>> observer = captor.getAllValues().get(0);
        final List<City> cities = mock(List.class);
        observer.onChanged(cities);
        verify(view, times(1)).showLocationsSearched(cities);
    }

    @Test
    public void verifySearchedDataDisplayedWhenAvailableOnHolder() {
        final List<City> content = mock(List.class);
        final LiveData<List<City>> data = mock(LiveData.class);
        when(data.getValue()).thenReturn(content);

        final LiveResult<List<City>> result = mock(LiveResult.class);
        when(result.data()).thenReturn(data);

        when(dataHolder.locationSearchesResult()).thenReturn(result);

        presenter.loadLocationsSearched();
        verify(view, times(1)).showLocationsSearched(content);
    }

    @Test
    public void verifySearchedDataFetchedWhenNotAvailableOnHolder() {
        when(dataHolder.locationSearchesResult()).thenReturn(null);
        final LiveResult<List<City>> result = mock(LiveResult.class);
        when(fetchSearchesUseCase.executeLive(any(), any(), any())).thenReturn(result);

        presenter.loadLocationsSearched();
        verify(fetchSearchesUseCase, times(1)).executeLive(any(), any(), any());
    }

    @Test
    public void verifyWeatherFetchedEvenWhenAvailableOnHolder() {
        final LiveData<Weather> oldData = mock(LiveData.class);
        final LiveResult<Weather> result = mock(LiveResult.class);
        when(result.data()).thenReturn(oldData);
        when(dataHolder.weatherResult()).thenReturn(result);
        when(fetchWeatherUseCase.executeLive(anyString(), any(), any())).thenReturn(result);

        presenter.loadWeatherForLocation("Location");
        verify(fetchWeatherUseCase, times(1)).executeLive(anyString(), any(), any());
    }

    @Test
    public void verifyScreenModelsDisplayedFromDataHandlerConversion() {
        final Weather value = mock(Weather.class);
        final WeatherScreenModel model = mock(WeatherScreenModel.class);
        when(screenConverter.prepareForPresentation(value)).thenReturn(model);

        presenter.handleWeatherData(value);

        verify(view, times(1)).hideProgress();
        verify(view, times(1)).resetInteractions();
        verify(view, times(1)).showWeather(model);
    }

    @Test
    public void verifyLastWeatherNotLoadedWhenNotAvailableOnHolder() {
        when(dataHolder.weatherResult()).thenReturn(null);
        presenter.loadLastWeatherIfAvailable();
        verify(view, never()).showWeather(any(WeatherScreenModel.class));
    }

    @Test
    public void verifyLastWeatherIsLoadedWhenAvailableOnHolder() {
        final LiveData<Weather> data = mock(LiveData.class);
        final Weather value = mock(Weather.class);
        when(data.getValue()).thenReturn(value);
        final LiveResult<Weather> result = mock(LiveResult.class);
        when(result.data()).thenReturn(data);
        when(dataHolder.weatherResult()).thenReturn(result);

        final WeatherScreenModel model = mock(WeatherScreenModel.class);
        when(screenConverter.prepareForPresentation(value)).thenReturn(model);

        presenter.loadLastWeatherIfAvailable();
        verify(view, times(1)).showWeather(model);
    }

}
