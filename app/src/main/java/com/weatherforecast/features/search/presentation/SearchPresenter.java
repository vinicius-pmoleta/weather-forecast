package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.core.validator.LiveDataValidator;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.search.data.Weather;
import com.weatherforecast.features.search.usecase.FetchLocationsSearchedUseCase;
import com.weatherforecast.features.search.usecase.FetchWeatherUseCase;

import java.util.List;

import static android.support.annotation.VisibleForTesting.PRIVATE;

@SuppressWarnings("ConstantConditions")
public class SearchPresenter implements SearchContract.Action {

    private final SearchContract.View view;
    private final FetchWeatherUseCase fetchWeatherUseCase;
    private final FetchLocationsSearchedUseCase fetchSearchesUseCase;

    public SearchPresenter(@NonNull final SearchContract.View view,
                           @NonNull final FetchWeatherUseCase fetchWeatherUseCase,
                           @NonNull final FetchLocationsSearchedUseCase fetchSearchesUseCase) {
        this.view = view;
        this.fetchWeatherUseCase = fetchWeatherUseCase;
        this.fetchSearchesUseCase = fetchSearchesUseCase;
    }

    @Override
    public void loadWeatherForLocation(@NonNull final String location) {
        final SearchDataHolder holder = view.provideSearchDataHolder();
        loadRemoteWeather(location, holder);
    }

    @Override
    public void loadLocationsSearched() {
        final SearchDataHolder holder = view.provideSearchDataHolder();
        if (LiveDataValidator.isContentAvailable(holder.locationSearchesData())) {
            handleLocationsSearchedData(holder.locationSearchesData().getValue());
            return;
        }

        loadLocalSearches(holder);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void loadRemoteWeather(@NonNull final String location, @NonNull final SearchDataHolder holder) {
        final LiveData<Weather> data = fetchWeatherUseCase.executeLive(location,
                holder::addSubscription,
                error -> view.showErrorLoadingWeather(),
                new UseCase.DefaultOnComplete());

        final LifecycleOwner owner = view.provideLifecycleOwner();
        removeDataObservers(holder.weatherData(), owner);
        holder.weatherData(data);
        data.observe(owner, this::handleWeatherData);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    <T> void removeDataObservers(@NonNull final LiveData<T> data,
                                 @NonNull final LifecycleOwner owner) {
        if (data != null && data.hasObservers()) {
            data.removeObservers(owner);
        }
    }

    @VisibleForTesting
    void loadLocalSearches(@NonNull final SearchDataHolder holder) {
        final LiveData<List<City>> data = fetchSearchesUseCase.executeLive(null,
                holder::addSubscription,
                error -> view.showErrorLoadingWeather(),
                new UseCase.DefaultOnComplete());

        final LifecycleOwner owner = view.provideLifecycleOwner();
        removeDataObservers(holder.locationSearchesData(), owner);
        holder.locationSearchesData(data);
        data.observe(owner, this::handleLocationsSearchedData);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void handleWeatherData(@NonNull final Weather weather) {
        view.showWeather(weather);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void handleLocationsSearchedData(@NonNull final List<City> cities) {
        view.showLocationsSearched(cities);
    }
}
