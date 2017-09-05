package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;

import com.weatherforecast.R;
import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.structure.BaseActivity;
import com.weatherforecast.features.search.di.DaggerForecastSearchFeatureComponent;
import com.weatherforecast.features.search.di.ForecastSearchPresentationModule;

public class ForecastSearchActivity extends BaseActivity<ForecastSearchPresenter> implements ForecastSearchContract.View {

    @Override
    public void initialiseDependencyInjector() {
        DaggerForecastSearchFeatureComponent.builder()
                .applicationComponent(((WeatherForecastApplication) getApplication()).applicationComponent())
                .forecastSearchPresentationModule(new ForecastSearchPresentationModule(this))
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_search_activity);

        initialiseTestTrigger();
    }

    private void initialiseTestTrigger() {
        findViewById(R.id.forecast_test).setOnClickListener(
                view -> presenter.loadLocationForecast("London,UK"));
    }

    @Override
    public LifecycleOwner provideLifecycleOwner() {
        return this;
    }
}
