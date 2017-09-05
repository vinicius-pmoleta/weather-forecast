package com.weatherforecast.features.search.di;

import android.support.annotation.NonNull;

import com.weatherforecast.core.di.scope.ActivityScope;
import com.weatherforecast.features.search.presentation.ForecastSearchContract;
import com.weatherforecast.features.search.presentation.ForecastSearchPresenter;
import com.weatherforecast.features.search.usecase.FetchLocalForecastUseCase;
import com.weatherforecast.features.search.usecase.FetchRemoteForecastUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class ForecastSearchPresentationModule {

    private final ForecastSearchContract.View view;

    public ForecastSearchPresentationModule(@NonNull final ForecastSearchContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    public ForecastSearchContract.View provideView() {
        return view;
    }

    @ActivityScope
    @Provides
    public ForecastSearchPresenter providePresenter(@NonNull final FetchRemoteForecastUseCase fetchRemoteUseCase,
                                                    @NonNull final FetchLocalForecastUseCase fetchLocalUseCase) {
        return new ForecastSearchPresenter(view, fetchLocalUseCase, fetchRemoteUseCase);
    }

}
