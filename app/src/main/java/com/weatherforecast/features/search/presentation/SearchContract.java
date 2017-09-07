package com.weatherforecast.features.search.presentation;

import android.support.annotation.NonNull;

import com.weatherforecast.core.structure.BaseContract;
import com.weatherforecast.features.search.data.Weather;

public interface SearchContract {

    interface View extends BaseContract.View {

        WeatherDataHolder provideWeatherDataHolder();

        void showWeather(@NonNull final Weather weather);

        void showErrorLoadingWeather();

    }

    interface Action extends BaseContract.Action {

        void loadWeatherForLocation(@NonNull final String location);

    }

}
