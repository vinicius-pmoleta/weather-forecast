package com.weatherforecast.features.dailyforecast.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.core.validator.LiveDataValidator;
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
    public void loadLocationForecast(@NonNull final Long id) {
        final DailyForecastDataHolder holder = view.provideForecastsDataHolder();
        if (LiveDataValidator.isContentAvailable(holder.data())) {
            handleDailyForecastsData(holder.data().getValue());
            return;
        }

        loadLocalForecast(id, holder);
        updateRemoteForecast(id, holder);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void loadLocalForecast(@NonNull final Long id, @NonNull final DailyForecastDataHolder holder) {
        final LiveData<List<DailyForecast>> data = fetchLocalUseCase.executeLive(id,
                holder::addSubscription,
                error -> view.showErrorLoadingDailyForecast(),
                new UseCase.DefaultOnComplete());

        holder.data(data);
        data.observe(view.provideLifecycleOwner(), this::handleDailyForecastsData);
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void updateRemoteForecast(@NonNull final Long id, @NonNull final DailyForecastDataHolder holder) {
        updateLocalUseCase.execute(id,
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
