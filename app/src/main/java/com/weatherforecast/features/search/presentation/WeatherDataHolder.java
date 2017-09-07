package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.weatherforecast.core.structure.BaseDataHolder;
import com.weatherforecast.features.search.data.Weather;

public class WeatherDataHolder extends BaseDataHolder {

    private LiveData<Weather> data;

    public LiveData<Weather> data() {
        return data;
    }

    public void data(@NonNull final LiveData<Weather> data) {
        this.data = data;
    }

}
