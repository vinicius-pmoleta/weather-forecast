package com.weatherforecast.core.di.module;

import com.weatherforecast.core.data.usecase.ExecutionConfiguration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class ConfigurationModule {

    @Provides
    @Singleton
    public ExecutionConfiguration provideExecutionConfiguration() {
        return new ExecutionConfiguration(Schedulers.io(), AndroidSchedulers.mainThread());
    }

}
