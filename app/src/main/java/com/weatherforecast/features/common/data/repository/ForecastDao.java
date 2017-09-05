package com.weatherforecast.features.common.data.repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.weatherforecast.features.common.data.entity.ForecastEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ForecastDao {

    @Insert
    void insertForecast(@NonNull final ForecastEntity forecast);

    @Query("SELECT * FROM forecast INNER JOIN city ON city.id = forecast.cityId WHERE city.id = :cityId")
    Flowable<List<ForecastEntity>> findForecastForCity(final long cityId);

}
