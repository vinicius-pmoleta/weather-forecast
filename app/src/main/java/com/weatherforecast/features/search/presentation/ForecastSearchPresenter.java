package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.weatherforecast.core.data.usecase.LiveUseCase;
import com.weatherforecast.core.structure.BaseContract;
import com.weatherforecast.features.common.data.model.Forecasts;
import com.weatherforecast.features.search.usecase.FetchLocationForecastRemoteUseCase;

import static android.support.annotation.VisibleForTesting.PRIVATE;

public class ForecastSearchPresenter implements BaseContract.Action {

    private final ForecastSearchContract.View view;
    private final FetchLocationForecastRemoteUseCase fetchRemoteUseCase;

    public ForecastSearchPresenter(@NonNull final ForecastSearchContract.View view,
                                   @NonNull final FetchLocationForecastRemoteUseCase fetchRemoteUseCase) {
        this.view = view;
        this.fetchRemoteUseCase = fetchRemoteUseCase;
    }

    public void loadLocationForecast(@NonNull final String location) {
        final ForecastsDataHolder holder = view.provideForecastsDataHolder();
        if (holder.data() != null) {
            handleForecastsData(holder.data().getValue());
            return;
        }

        final LiveData<Forecasts> data = fetchRemoteUseCase.execute(location,
                holder::addSubscription,
                error -> view.showErrorLoadingLocationForecast(),
                new LiveUseCase.DefaultOnComplete());

        holder.data(data);
        data.observe(view.provideLifecycleOwner(), this::handleForecastsData);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void handleForecastsData(Forecasts forecasts) {
        Log.d("TEST", forecasts.city().name());
    }
}
