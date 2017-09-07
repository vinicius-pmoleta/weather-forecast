package com.weatherforecast.features.search.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.di.component.ApplicationComponent;
import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.search.presentation.SearchActivity;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {SearchPresentationModule.class, SearchUseCaseModule.class}
)
public interface SearchFeatureComponent {

    void inject(@NonNull final SearchActivity activity);

}
