package com.weatherforecast.features.dailyforecast.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;
import com.weatherforecast.features.dailyforecast.usecase.FetchLocalForecastUseCase;
import com.weatherforecast.features.dailyforecast.usecase.UpdateLocalForecastUseCase;

import java.util.List;

import static android.support.annotation.VisibleForTesting.PRIVATE;

@SuppressWarnings("ConstantConditions")
public class DailyForecastPresenter implements DailyForecastContract.Action {

    private final DailyForecastContract.View view;
    private final FetchLocalForecastUseCase fetchLocalUseCase;
    private final UpdateLocalForecastUseCase updateLocalUseCase;

    public DailyForecastPresenter(@NonNull final DailyForecastContract.View view,
                                  @NonNull final FetchLocalForecastUseCase fetchLocalUseCase,
                                  @NonNull final UpdateLocalForecastUseCase updateLocalUseCase) {
        this.view = view;
        this.updateLocalUseCase = updateLocalUseCase;
        this.fetchLocalUseCase = fetchLocalUseCase;
    }

    @Override
    public void loadLocationForecast(@Nullable final Long id, @NonNull final String location) {
        final DailyForecastDataHolder holder = view.provideForecastsDataHolder();
        if (isContentAvailableOnHolder(holder)) {
            handleDailyForecastsData(holder.data().getValue());
            return;
        }

        loadLocalForecast(id, holder);
        updateRemoteForecast(location, holder);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    boolean isContentAvailableOnHolder(@NonNull final DailyForecastDataHolder holder) {
        return holder.data() != null && holder.data().getValue() != null;
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void loadLocalForecast(@Nullable final Long id, @NonNull final DailyForecastDataHolder holder) {
        final LiveData<List<DailyForecast>> data = fetchLocalUseCase.executeLive(id,
                holder::addSubscription,
                error -> view.showErrorLoadingDailyForecast(),
                new UseCase.DefaultOnComplete());

        holder.data(data);
        data.observe(view.provideLifecycleOwner(), this::handleDailyForecastsData);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void updateRemoteForecast(@NonNull final String location, @NonNull final DailyForecastDataHolder holder) {
        updateLocalUseCase.execute(location,
                holder::addSubscription,
                error -> view.showErrorLoadingDailyForecast(),
                new UseCase.DefaultOnComplete()
        ).subscribe();
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void handleDailyForecastsData(@NonNull final List<DailyForecast> dailyForecasts) {
        view.showDailyForecasts(dailyForecasts);
    }
}
