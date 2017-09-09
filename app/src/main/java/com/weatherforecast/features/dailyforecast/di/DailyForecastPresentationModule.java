package com.weatherforecast.features.dailyforecast.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.dailyforecast.presentation.DailyForecastContract;
import com.weatherforecast.features.dailyforecast.presentation.DailyForecastPresenter;
import com.weatherforecast.features.dailyforecast.presentation.model.DailyForecastScreenConverter;
import com.weatherforecast.features.dailyforecast.usecase.FetchLocalForecastUseCase;
import com.weatherforecast.features.dailyforecast.usecase.UpdateLocalForecastUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class DailyForecastPresentationModule {

    private final DailyForecastContract.View view;

    public DailyForecastPresentationModule(@NonNull final DailyForecastContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    public DailyForecastContract.View provideView() {
        return view;
    }

    @ActivityScope
    @Provides
    public DailyForecastScreenConverter provideScreenConverter(@NonNull final WeatherForecastApplication application) {
        return DailyForecastScreenConverter.newInstance(application.getApplicationContext());
    }

    @ActivityScope
    @Provides
    public DailyForecastPresenter providePresenter(@NonNull final UpdateLocalForecastUseCase fetchRemoteUseCase,
                                                   @NonNull final FetchLocalForecastUseCase fetchLocalUseCase,
                                                   @NonNull final DailyForecastScreenConverter screenConverter) {
        return new DailyForecastPresenter(view, fetchLocalUseCase, fetchRemoteUseCase, screenConverter);
    }

}
