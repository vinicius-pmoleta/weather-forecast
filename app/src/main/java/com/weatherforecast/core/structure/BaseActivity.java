package com.weatherforecast.core.structure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

public abstract class BaseActivity<Presenter extends BaseContract.Action> extends AppCompatActivity {

    @Inject
    protected Presenter presenter;

    public abstract void initialiseDependencyInjector();

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseDependencyInjector();
    }
}
