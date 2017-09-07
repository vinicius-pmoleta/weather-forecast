package com.weatherforecast.features.search.presentation;

import android.app.SearchManager;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.weatherforecast.R;
import com.weatherforecast.core.structure.BaseActivity;
import com.weatherforecast.features.dailyforecast.presentation.DailyForecastActivity;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    @Override
    public void initialiseDependencyInjector() {

    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initialiseNavigation();
        initialiseTestTrigger();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull final Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.search);
        searchItem.getIcon().mutate().setColorFilter(
                ContextCompat.getColor(this, R.color.toolbarColorControlNormal), PorterDuff.Mode.SRC_ATOP);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private void initialiseNavigation() {
        final Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initialiseTestTrigger() {
        findViewById(R.id.search_test).setOnClickListener(
                view -> startActivity(DailyForecastActivity.newIntent(this, 2643743L, "London,UK")));
    }

    @Override
    public LifecycleOwner provideLifecycleOwner() {
        return null;
    }


}
