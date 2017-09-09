package com.weatherforecast.features.dailyforecast.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.weatherforecast.core.data.live.LiveDataOperator;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;
import com.weatherforecast.features.dailyforecast.presentation.model.DailyForecastScreenConverter;
import com.weatherforecast.features.dailyforecast.presentation.model.DailyForecastScreenModel;
import com.weatherforecast.features.dailyforecast.usecase.FetchLocalForecastUseCase;
import com.weatherforecast.features.dailyforecast.usecase.UpdateLocalForecastUseCase;

import java.util.List;

import static android.support.annotation.VisibleForTesting.PRIVATE;

@SuppressWarnings("ConstantConditions")
public class DailyForecastPresenter implements DailyForecastContract.Action {

    private final DailyForecastContract.View view;
    private final FetchLocalForecastUseCase fetchLocalUseCase;
    private final UpdateLocalForecastUseCase updateLocalUseCase;
    private final DailyForecastScreenConverter screenConverter;

    public DailyForecastPresenter(@NonNull final DailyForecastContract.View view,
                                  @NonNull final FetchLocalForecastUseCase fetchLocalUseCase,
                                  @NonNull final UpdateLocalForecastUseCase updateLocalUseCase,
                                  @NonNull final DailyForecastScreenConverter screenConverter) {
        this.view = view;
        this.updateLocalUseCase = updateLocalUseCase;
        this.fetchLocalUseCase = fetchLocalUseCase;
        this.screenConverter = screenConverter;
    }

    @Override
    public void loadLocationForecast(@NonNull final Long id) {
        final DailyForecastDataHolder holder = view.provideForecastsDataHolder();
        if (LiveDataOperator.isContentAvailable(holder.data())) {
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
                new UseCase.DefaultOnError(),
                new UseCase.DefaultOnComplete()
        ).subscribe(
                result -> {
                },
                error -> view.showErrorLoadingDailyForecast()
        );
    }

    @VisibleForTesting(otherwise = PRIVATE)
    void handleDailyForecastsData(@NonNull final List<DailyForecast> dailyForecasts) {
        final List<DailyForecastScreenModel> screenModels = screenConverter.prepareForPresentation(dailyForecasts);
        view.showDailyForecasts(screenModels);
    }
}
