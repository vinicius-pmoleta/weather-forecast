package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.weatherforecast.core.structure.BaseDataHolder;
import com.weatherforecast.features.common.data.model.Forecast;

import java.util.List;

public class ForecastsDataHolder extends BaseDataHolder {

    private LiveData<List<Forecast>> data;

    public LiveData<List<Forecast>> data() {
        return data;
    }

    public void data(@NonNull final LiveData<List<Forecast>> data) {
        this.data = data;
    }

}
