package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.search.usecase.FetchLocalForecastUseCase;
import com.weatherforecast.features.search.usecase.FetchRemoteForecastUseCase;

import java.util.List;

import static android.support.annotation.VisibleForTesting.PRIVATE;

public class ForecastSearchPresenter implements ForecastSearchContract.Action {

    private final ForecastSearchContract.View view;
    private final FetchLocalForecastUseCase fetchLocalUseCase;
    private final FetchRemoteForecastUseCase fetchRemoteUseCase;

    public ForecastSearchPresenter(@NonNull final ForecastSearchContract.View view,
                                   @NonNull final FetchLocalForecastUseCase fetchLocalUseCase,
                                   @NonNull final FetchRemoteForecastUseCase fetchRemoteUseCase) {
        this.view = view;
        this.fetchRemoteUseCase = fetchRemoteUseCase;
        this.fetchLocalUseCase = fetchLocalUseCase;
    }

    @Override
    public void loadLocationForecast(@Nullable final Long id, @NonNull final String location) {
        final ForecastsDataHolder holder = view.provideForecastsDataHolder();
        if (holder.data() != null) {
            handleForecastsData(holder.data().getValue());
            return;
        }

        loadLocalForecast(id, holder);
        loadRemoteForecast(location, holder);
    }

    private void loadLocalForecast(@Nullable final Long id, @NonNull final ForecastsDataHolder holder) {
        final LiveData<List<Forecast>> data = fetchLocalUseCase.executeLive(id,
                holder::addSubscription,
                error -> view.showErrorLoadingLocationForecast(),
                new UseCase.DefaultOnComplete());

        holder.data(data);
        data.observe(view.provideLifecycleOwner(), this::handleForecastsData);
    }

    private void loadRemoteForecast(@NonNull final String location, @NonNull final ForecastsDataHolder holder) {
        fetchRemoteUseCase.execute(location,
                holder::addSubscription,
                error -> view.showErrorLoadingLocationForecast(),
                new UseCase.DefaultOnComplete());
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void handleForecastsData(@Nullable final List<Forecast> forecasts) {
        Log.d("TEST", String.valueOf(forecasts.size()));
    }
}
