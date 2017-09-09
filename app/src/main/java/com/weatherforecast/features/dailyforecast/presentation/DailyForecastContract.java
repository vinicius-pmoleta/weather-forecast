package com.weatherforecast.features.dailyforecast.presentation;

import android.support.annotation.NonNull;

import com.weatherforecast.core.structure.BaseContract;
import com.weatherforecast.features.dailyforecast.presentation.model.DailyForecastScreenModel;

import java.util.List;

public interface DailyForecastContract {

    interface View extends BaseContract.View {

        DailyForecastDataHolder provideForecastsDataHolder();

        void showErrorLoadingDailyForecast();

        void showDailyForecasts(@NonNull final List<DailyForecastScreenModel> dailyForecasts);

    }

    interface Action extends BaseContract.Action {

        void loadLocationForecast(@NonNull final Long id);
    }

}
