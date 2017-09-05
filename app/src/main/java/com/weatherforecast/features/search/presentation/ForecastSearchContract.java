package com.weatherforecast.features.search.presentation;

import com.weatherforecast.core.structure.BaseContract;

public interface ForecastSearchContract {

    interface View extends BaseContract.View {

        ForecastsDataHolder provideForecastsDataHolder();

        void showErrorLoadingLocationForecast();
    }

    interface Action extends BaseContract.Action {

    }

}
