package com.weatherforecast.features.search.presentation;

import android.support.annotation.NonNull;

import com.weatherforecast.core.structure.BaseContract;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.search.presentation.model.WeatherScreenModel;

import java.util.List;

public interface SearchContract {

    interface View extends BaseContract.View {

        SearchDataHolder provideSearchDataHolder();

        void showWeather(@NonNull final WeatherScreenModel weather);

        void showErrorLoadingWeather();

        void showLocationsSearched(@NonNull final List<City> cities);

        void showProgress();

        void hideProgress();

        void resetInteractions();
    }

    interface Action extends BaseContract.Action {

        void loadWeatherForLocation(@NonNull final String location);

        void loadLocationsSearched();
    }

}
