package com.weatherforecast.features.search.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.search.presentation.SearchContract;
import com.weatherforecast.features.search.presentation.SearchPresenter;
import com.weatherforecast.features.search.presentation.model.WeatherScreenConverter;
import com.weatherforecast.features.search.usecase.FetchLocationsSearchedUseCase;
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
    public WeatherScreenConverter provideScreenConverter(@NonNull final WeatherForecastApplication application) {
        return WeatherScreenConverter.newInstance(application.getApplicationContext());
    }

    @ActivityScope
    @Provides
    public SearchPresenter providePresenter(@NonNull final FetchWeatherUseCase fetchWeatherUseCase,
                                            @NonNull final FetchLocationsSearchedUseCase fetchSearchesUseCase,
                                            @NonNull final WeatherScreenConverter screenConverter) {
        return new SearchPresenter(view, fetchWeatherUseCase, fetchSearchesUseCase, screenConverter);
    }

}
