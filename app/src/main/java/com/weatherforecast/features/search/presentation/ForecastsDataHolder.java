package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.weatherforecast.core.structure.BaseDataHolder;
import com.weatherforecast.features.common.data.model.Forecasts;

public class ForecastsDataHolder extends BaseDataHolder {

    private LiveData<Forecasts> data;

    public LiveData<Forecasts> data() {
        return data;
    }

    public void data(@NonNull final LiveData<Forecasts> data) {
        this.data = data;
    }

}
