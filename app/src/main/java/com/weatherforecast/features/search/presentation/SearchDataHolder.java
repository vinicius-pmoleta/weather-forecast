package com.weatherforecast.features.search.presentation;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.weatherforecast.core.structure.BaseDataHolder;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.search.data.Weather;

import java.util.List;

public class SearchDataHolder extends BaseDataHolder {

    private LiveData<Weather> weatherData;
    private LiveData<List<City>> locationsData;

    LiveData<Weather> weatherData() {
        return weatherData;
    }

    void weatherData(@NonNull final LiveData<Weather> data) {
        this.weatherData = data;
    }

    LiveData<List<City>> locationSearchesData() {
        return locationsData;
    }

    void locationSearchesData(@NonNull final LiveData<List<City>> data) {
        this.locationsData = data;
    }

}
