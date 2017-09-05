package com.weatherforecast.features.search.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.structure.BaseContract;

public interface ForecastSearchContract {

    interface View extends BaseContract.View {

        ForecastsDataHolder provideForecastsDataHolder();

        void showErrorLoadingLocationForecast();
    }

    interface Action extends BaseContract.Action {

        void loadLocationForecast(@Nullable Long id, @NonNull String location);
    }

}
