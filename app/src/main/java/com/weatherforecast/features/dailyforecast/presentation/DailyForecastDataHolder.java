package com.weatherforecast.features.dailyforecast.presentation;

import android.support.annotation.NonNull;

import com.weatherforecast.core.data.live.LiveResult;
import com.weatherforecast.core.structure.BaseDataHolder;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;

import java.util.List;

public class DailyForecastDataHolder extends BaseDataHolder {

    private LiveResult<List<DailyForecast>> result;

    public LiveResult<List<DailyForecast>> result() {
        return result;
    }

    public void result(@NonNull final LiveResult<List<DailyForecast>> result) {
        this.result = result;
    }

}
