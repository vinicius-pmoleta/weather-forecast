package com.weatherforecast.features.dailyforecast.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weatherforecast.core.structure.BaseContract;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;

import java.util.List;

public interface DailyForecastContract {

    interface View extends BaseContract.View {

        DailyForecastDataHolder provideForecastsDataHolder();

        void showErrorLoadingDailyForecast();

        void showDailyForecasts(@NonNull final List<DailyForecast> dailyForecasts);

    }

    interface Action extends BaseContract.Action {

        void loadLocationForecast(@Nullable final Long id, @NonNull final String location);
    }

}
