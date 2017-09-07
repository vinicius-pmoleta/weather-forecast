package com.weatherforecast.core.structure;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

public abstract class BaseActivity<Presenter extends BaseContract.Action> extends AppCompatLifecycleActivity {

    @Inject
    protected Presenter presenter;

    public abstract void initialiseDependencyInjector();

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseDependencyInjector();
    }
}
