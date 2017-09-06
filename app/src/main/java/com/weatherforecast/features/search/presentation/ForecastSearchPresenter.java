package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.search.data.model.DailyForecast;
import com.weatherforecast.features.search.usecase.FetchLocalForecastUseCase;
import com.weatherforecast.features.search.usecase.UpdateLocalForecastUseCase;

import java.util.List;

import static android.support.annotation.VisibleForTesting.PRIVATE;

public class ForecastSearchPresenter implements ForecastSearchContract.Action {

    private final ForecastSearchContract.View view;
    private final FetchLocalForecastUseCase fetchLocalUseCase;
    private final UpdateLocalForecastUseCase updateLocalUseCase;

    public ForecastSearchPresenter(@NonNull final ForecastSearchContract.View view,
                                   @NonNull final FetchLocalForecastUseCase fetchLocalUseCase,
                                   @NonNull final UpdateLocalForecastUseCase updateLocalUseCase) {
        this.view = view;
        this.updateLocalUseCase = updateLocalUseCase;
        this.fetchLocalUseCase = fetchLocalUseCase;
    }

    @Override
    public void loadLocationForecast(@Nullable final Long id, @NonNull final String location) {
        final ForecastsDataHolder holder = view.provideForecastsDataHolder();
        if (holder.data() != null && holder.data().getValue() != null) {
            handleDailyForecastsData(holder.data().getValue());
            return;
        }

        loadLocalForecast(id, holder);
        updateRemoteForecast(location, holder);
    }

    private void loadLocalForecast(@Nullable final Long id, @NonNull final ForecastsDataHolder holder) {
        final LiveData<List<DailyForecast>> data = fetchLocalUseCase.executeLive(id,
                holder::addSubscription,
                error -> view.showErrorLoadingLocationForecast(),
                new UseCase.DefaultOnComplete());

        holder.data(data);
        data.observe(view.provideLifecycleOwner(), this::handleDailyForecastsData);
    }

    private void updateRemoteForecast(@NonNull final String location, @NonNull final ForecastsDataHolder holder) {
        updateLocalUseCase.execute(location,
                holder::addSubscription,
                error -> view.showErrorLoadingLocationForecast(),
                new UseCase.DefaultOnComplete()
        ).subscribe();
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void handleDailyForecastsData(@NonNull final List<DailyForecast> dailyForecasts) {
        Log.d("TEST", String.valueOf(dailyForecasts.size()));
        for (final DailyForecast dailyForecast : dailyForecasts) {
            for (final Forecast forecast : dailyForecast.forecasts()) {
                Log.d("TEST", "KEY " + dailyForecast.date() + " / VALUE " + String.valueOf(forecast.date()));
            }
        }
    }
}
