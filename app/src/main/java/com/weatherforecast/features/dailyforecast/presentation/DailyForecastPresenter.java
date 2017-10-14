package com.weatherforecast.features.dailyforecast.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.weatherforecast.core.data.live.LiveDataOperator;
import com.weatherforecast.core.data.live.LiveResult;
import com.weatherforecast.core.data.usecase.UseCase;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;
import com.weatherforecast.features.dailyforecast.presentation.model.DailyForecastScreenConverter;
import com.weatherforecast.features.dailyforecast.presentation.model.DailyForecastScreenModel;
import com.weatherforecast.features.dailyforecast.usecase.FetchLocalForecastUseCase;
import com.weatherforecast.features.dailyforecast.usecase.UpdateLocalForecastUseCase;

import java.util.List;

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
        if (LiveDataOperator.isDataAvailable(holder.result())) {
            handleDailyForecastsData(holder.result().data().getValue());
            return;
        }

        loadLocalForecast(id, holder);
        updateRemoteForecast(id, holder);
    }

    @Override
    public void refreshLocationForecast(@NonNull final Long id) {
        final DailyForecastDataHolder holder = view.provideForecastsDataHolder();
        updateRemoteForecast(id, holder);
    }

    @VisibleForTesting
    void loadLocalForecast(@NonNull final Long id, @NonNull final DailyForecastDataHolder holder) {
        final LiveResult<List<DailyForecast>> result = fetchLocalUseCase.executeLive(id,
                holder::addSubscription,
                new UseCase.DefaultOnComplete());

        holder.result(result);
        result.observe(view.provideLifecycleOwner(), this::handleDailyForecastsData, error -> handleDailyForecastsError());
    }

    @VisibleForTesting
    void updateRemoteForecast(@NonNull final Long id, @NonNull final DailyForecastDataHolder holder) {
        updateLocalUseCase.execute(id, holder::addSubscription)
                .subscribe(
                        result -> {
                        },
                        error -> handleDailyForecastsError()
                );
    }

    @VisibleForTesting
    void handleDailyForecastsData(@NonNull final List<DailyForecast> dailyForecasts) {
        view.hideProgress();
        final List<DailyForecastScreenModel> screenModels = screenConverter.prepareForPresentation(dailyForecasts);
        view.showDailyForecasts(screenModels);
    }

    private void handleDailyForecastsError() {
        view.showErrorLoadingDailyForecast();
        view.hideProgress();
    }
}
