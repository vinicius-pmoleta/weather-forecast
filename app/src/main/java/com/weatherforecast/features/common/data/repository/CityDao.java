package com.weatherforecast.features.common.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.weatherforecast.features.common.data.entity.City;

import java.util.List;

@Dao
public interface CityDao {

    @Insert
    void insertCity(@NonNull final City city);

    @Query("SELECT * FROM city")
    LiveData<List<City>> all();

}
