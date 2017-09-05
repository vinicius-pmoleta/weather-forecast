package com.weatherforecast.features.search.presentation;

import android.support.annotation.NonNull;

import com.weatherforecast.core.structure.BaseContract;

public class ForecastSearchPresenter implements BaseContract.Action {

    private final ForecastSearchContract.View view;

    public ForecastSearchPresenter(@NonNull final ForecastSearchContract.View view) {
        this.view = view;
    }

}
