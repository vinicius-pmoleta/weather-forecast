package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.weatherforecast.core.data.live.LiveDataOperator;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.search.data.Weather;
import com.weatherforecast.features.search.presentation.model.WeatherScreenConverter;
import com.weatherforecast.features.search.presentation.model.WeatherScreenModel;
import com.weatherforecast.features.search.usecase.FetchLocationsSearchedUseCase;
import com.weatherforecast.features.search.usecase.FetchWeatherUseCase;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class SearchPresenter implements SearchContract.Action {

    private final SearchContract.View view;
    private final FetchWeatherUseCase fetchWeatherUseCase;
    private final FetchLocationsSearchedUseCase fetchSearchesUseCase;
    private final WeatherScreenConverter screenConverter;

    public SearchPresenter(@NonNull final SearchContract.View view,
                           @NonNull final FetchWeatherUseCase fetchWeatherUseCase,
                           @NonNull final FetchLocationsSearchedUseCase fetchSearchesUseCase,
                           @NonNull final WeatherScreenConverter screenConverter) {
        this.view = view;
        this.fetchWeatherUseCase = fetchWeatherUseCase;
        this.fetchSearchesUseCase = fetchSearchesUseCase;
        this.screenConverter = screenConverter;
    }

    @Override
    public void loadWeatherForLocation(@NonNull final String location) {
        view.showProgress();

        final SearchDataHolder holder = view.provideSearchDataHolder();
        loadRemoteWeather(location, holder);
    }

    @Override
    public void loadLastWeatherIfAvailable() {
        final SearchDataHolder holder = view.provideSearchDataHolder();
        if (LiveDataOperator.isContentAvailable(holder.weatherData())) {
            handleWeatherData(holder.weatherData().getValue());
        }
    }

    @Override
    public void loadLocationsSearched() {
        final SearchDataHolder holder = view.provideSearchDataHolder();
        if (LiveDataOperator.isContentAvailable(holder.locationSearchesData())) {
            handleLocationsSearchedData(holder.locationSearchesData().getValue());
            return;
        }

        loadLocalSearches(holder);
    }

    @VisibleForTesting
    void loadRemoteWeather(@NonNull final String location, @NonNull final SearchDataHolder holder) {
        final LiveData<Weather> data = fetchWeatherUseCase.executeLive(location,
                holder::addSubscription,
                error -> {
                    view.hideProgress();
                    view.showErrorLoadingWeather();
                },
                new UseCase.DefaultOnComplete());

        final LifecycleOwner owner = view.provideLifecycleOwner();
        LiveDataOperator.removeDataObservers(holder.weatherData(), owner);
        holder.weatherData(data);
        data.observe(owner, this::handleWeatherData);
    }

    @VisibleForTesting
    void loadLocalSearches(@NonNull final SearchDataHolder holder) {
        final LiveData<List<City>> data = fetchSearchesUseCase.executeLive(null,
                holder::addSubscription,
                error -> view.showErrorLoadingWeather(),
                new UseCase.DefaultOnComplete());

        final LifecycleOwner owner = view.provideLifecycleOwner();
        LiveDataOperator.removeDataObservers(holder.locationSearchesData(), owner);
        holder.locationSearchesData(data);
        data.observe(owner, this::handleLocationsSearchedData);
    }

    @VisibleForTesting
    void handleWeatherData(@NonNull final Weather weather) {
        view.hideProgress();
        view.resetInteractions();
        final WeatherScreenModel screenModel = screenConverter.prepareForPresentation(weather);
        view.showWeather(screenModel);
    }

    private void handleLocationsSearchedData(@NonNull final List<City> cities) {
        view.showLocationsSearched(cities);
    }
}
