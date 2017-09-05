package com.weatherforecast.features.search.presentation;

import android.support.annotation.NonNull;

import com.weatherforecast.core.structure.BaseContract;
import com.weatherforecast.features.search.usecase.FetchLocationForecastRemoteUseCase;

public class ForecastSearchPresenter implements BaseContract.Action {

    private final ForecastSearchContract.View view;
    private final FetchLocationForecastRemoteUseCase fetchRemoteUseCase;

    public ForecastSearchPresenter(@NonNull final ForecastSearchContract.View view,
                                   @NonNull final FetchLocationForecastRemoteUseCase fetchRemoteUseCase) {
        this.view = view;
        this.fetchRemoteUseCase = fetchRemoteUseCase;
    }

    public void loadLocationForecast(@NonNull final String location) {
        fetchRemoteUseCase.execute(location, null, null, null);
    }
}
