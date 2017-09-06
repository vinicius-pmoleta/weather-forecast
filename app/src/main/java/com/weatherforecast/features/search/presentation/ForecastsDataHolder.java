package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.weatherforecast.core.structure.BaseDataHolder;
import com.weatherforecast.features.search.data.model.DailyForecast;

import java.util.List;

public class ForecastsDataHolder extends BaseDataHolder {

    private LiveData<List<DailyForecast>> data;

    public LiveData<List<DailyForecast>> data() {
        return data;
    }

    public void data(@NonNull final LiveData<List<DailyForecast>> data) {
        this.data = data;
    }

}
