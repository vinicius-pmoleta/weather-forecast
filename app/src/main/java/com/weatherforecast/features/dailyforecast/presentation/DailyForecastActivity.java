package com.weatherforecast.features.dailyforecast.presentation;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.weatherforecast.R;
import com.weatherforecast.core.WeatherForecastApplication;
import com.weatherforecast.core.structure.BaseActivity;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;
import com.weatherforecast.features.dailyforecast.di.DaggerDailyForecastFeatureComponent;
import com.weatherforecast.features.dailyforecast.di.DailyForecastPresentationModule;
import com.weatherforecast.features.dailyforecast.di.DailyForecastUseCaseModule;

import java.util.List;

public class DailyForecastActivity extends BaseActivity<DailyForecastPresenter> implements DailyForecastContract.View {

    private static final String EXTRA_ID = "extra_id";
    private static final String EXTRA_LOCATION = "extra_location";

    public static Intent newIntent(@NonNull final Context context, final long id, @NonNull final String location) {
        return new Intent(context, DailyForecastActivity.class)
                .putExtra(EXTRA_ID, id)
                .putExtra(EXTRA_LOCATION, location);
    }

    private ForecastByDateAdapter adapter;

    @Override
    public void initialiseDependencyInjector() {
        DaggerDailyForecastFeatureComponent.builder()
                .applicationComponent(((WeatherForecastApplication) getApplication()).applicationComponent())
                .dailyForecastPresentationModule(new DailyForecastPresentationModule(this))
                .dailyForecastUseCaseModule(new DailyForecastUseCaseModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_forecast_activity);

        initialiseNavigation();
        initialiseResult();
        extractExtrasAndLoadDailyForecast();
    }

    private void initialiseNavigation() {
        final Toolbar toolbar = findViewById(R.id.daily_forecast_toolbar);
        setSupportActionBar(toolbar);

        final String location = getIntent().getStringExtra(EXTRA_LOCATION);
        getSupportActionBar().setTitle(location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initialiseResult() {
        adapter = new ForecastByDateAdapter();

        final RecyclerView resultView = findViewById(R.id.daily_forecast_result);
        resultView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        resultView.setAdapter(adapter);
    }

    private void extractExtrasAndLoadDailyForecast() {
        final long id = getIntent().getLongExtra(EXTRA_ID, -1L);
        presenter.loadLocationForecast(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public LifecycleOwner provideLifecycleOwner() {
        return this;
    }

    @Override
    public DailyForecastDataHolder provideForecastsDataHolder() {
        return ViewModelProviders.of(this).get(DailyForecastDataHolder.class);
    }

    @Override
    public void showErrorLoadingDailyForecast() {
        Toast.makeText(this, R.string.daily_forecast_error_loading, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDailyForecasts(@NonNull final List<DailyForecast> dailyForecasts) {
        adapter.updateContent(dailyForecasts);

        Log.d("TEST", String.valueOf(dailyForecasts.size()));
        for (final DailyForecast dailyForecast : dailyForecasts) {
            for (final Forecast forecast : dailyForecast.forecasts()) {
                Log.d("TEST", "KEY " + dailyForecast.date() + " / VALUE " + String.valueOf(forecast.date()));
            }
        }
    }
}
