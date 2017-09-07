package com.weatherforecast.features.common.data.repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.weatherforecast.features.common.data.entity.CityEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(final CityEntity city);

    @Query("SELECT * FROM city")
    Flowable<List<CityEntity>> all();

}
