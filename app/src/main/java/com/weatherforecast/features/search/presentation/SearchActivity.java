package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.weatherforecast.R;
import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.structure.BaseActivity;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.dailyforecast.presentation.DailyForecastActivity;
import com.weatherforecast.features.search.data.Weather;
import com.weatherforecast.features.search.di.DaggerSearchFeatureComponent;
import com.weatherforecast.features.search.di.SearchPresentationModule;
import com.weatherforecast.features.search.di.SearchUseCaseModule;

import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    @Override
    public void initialiseDependencyInjector() {
        DaggerSearchFeatureComponent.builder()
                .applicationComponent(((WeatherForecastApplication) getApplication()).applicationComponent())
                .searchPresentationModule(new SearchPresentationModule(this))
                .searchUseCaseModule(new SearchUseCaseModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initialiseTestTrigger();
        initializeSearchInteraction();
        initialiseSuggestions();
    }

    private void initialiseTestTrigger() {
        findViewById(R.id.search_test).setOnClickListener(
                view -> startActivity(DailyForecastActivity.newIntent(this, 2643743L, "London,UK")));
    }

    private void initializeSearchInteraction() {
        final EditText queryField = findViewById(R.id.search_query);
        findViewById(R.id.search_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loadWeatherForLocation(queryField.getText().toString());
            }
        });
    }

    private void initialiseSuggestions() {
        presenter.loadLocationsSearched();
    }

    @Override
    public LifecycleOwner provideLifecycleOwner() {
        return this;
    }


    @Override
    public SearchDataHolder provideSearchDataHolder() {
        return ViewModelProviders.of(this).get(SearchDataHolder.class);
    }

    @Override
    public void showWeather(@NonNull final Weather weather) {
        Log.d("TEST", weather.toString());
    }

    @Override
    public void showErrorLoadingWeather() {
        Toast.makeText(this, R.string.search_weather_error_loading, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLocationsSearched(@NonNull final List<City> cities) {
        for (final City city : cities) {
            Log.d("TEST", city.toString());
        }
    }
}
