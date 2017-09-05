package com.weatherforecast.features.search.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.di.component.ApplicationComponent;
import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.search.presentation.ForecastSearchActivity;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {ForecastSearchPresentationModule.class, ForecastSearchUseCaseModule.class}
)
public interface ForecastSearchFeatureComponent {

    void inject(@NonNull final ForecastSearchActivity activity);

}
