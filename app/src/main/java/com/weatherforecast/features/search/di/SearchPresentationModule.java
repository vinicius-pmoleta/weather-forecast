package com.weatherforecast.features.search.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.search.presentation.SearchContract;
import com.weatherforecast.features.search.presentation.SearchPresenter;
import com.weatherforecast.features.search.usecase.FetchWeatherUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchPresentationModule {

    private final SearchContract.View view;

    public SearchPresentationModule(@NonNull final SearchContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    public SearchContract.View provideView() {
        return view;
    }

    @ActivityScope
    @Provides
    public SearchPresenter providePresenter(@NonNull final FetchWeatherUseCase fetchWeatherUseCase) {
        return new SearchPresenter(view, fetchWeatherUseCase);
    }

}
