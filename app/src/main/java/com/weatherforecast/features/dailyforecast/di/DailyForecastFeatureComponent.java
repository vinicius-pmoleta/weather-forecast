package com.weatherforecast.features.dailyforecast.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.di.component.ApplicationComponent;
import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.dailyforecast.presentation.DailyForecastActivity;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {DailyForecastPresentationModule.class, DailyForecastUseCaseModule.class}
)
public interface DailyForecastFeatureComponent {

    void inject(@NonNull final DailyForecastActivity activity);

}
