package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.search.data.Weather;
import com.weatherforecast.features.search.usecase.FetchWeatherUseCase;

import static android.support.annotation.VisibleForTesting.PRIVATE;

public class SearchPresenter implements SearchContract.Action {

    private final SearchContract.View view;
    private final FetchWeatherUseCase fetchWeatherUseCase;

    public SearchPresenter(@NonNull final SearchContract.View view, @NonNull final FetchWeatherUseCase fetchWeatherUseCase) {
        this.view = view;
        this.fetchWeatherUseCase = fetchWeatherUseCase;
    }

    @Override
    public void loadWeatherForLocation(@NonNull final String location) {
        final WeatherDataHolder holder = view.provideWeatherDataHolder();
        loadRemoteWeather(location, holder);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void loadRemoteWeather(@NonNull final String location, @NonNull final WeatherDataHolder holder) {
        final LiveData<Weather> data = fetchWeatherUseCase.executeLive(location,
                holder::addSubscription,
                error -> view.showErrorLoadingWeather(),
                new UseCase.DefaultOnComplete());

        final LifecycleOwner owner = view.provideLifecycleOwner();
        if (holder.data() != null) {
            holder.data().removeObservers(owner);
        }
        holder.data(data);
        data.observe(owner, this::handleWeatherData);
    }


    @VisibleForTesting(otherwise = PRIVATE)
    void handleWeatherData(@NonNull final Weather weather) {
        view.showWeather(weather);
    }
}
